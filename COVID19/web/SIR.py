# coding: utf-8
import sys
import numpy as np
import pandas as pd
import pymysql
from scipy.integrate import odeint


def fetch_data(conn, type, obj_name):  # 从数据库取最近7天的记录
    data_list = []
    cursor = conn.cursor()

    if type == 0:  # 国家级
        table_name = "country"
        sql = "select time,confirm_sum,cured_sum, dead_sum,name from %s where name ='%s'" % (table_name, obj_name)
        try:
            cursor.execute(sql)
            data_list = cursor.fetchall()
        except:
            conn.rollback()
            print("查询%s表失败" % table_name)
    elif type == 1:  # 省级
        table_name = "china_city"
        sql = "select time,confirm_sum,cured_sum, dead_sum,name from %s where province ='%s'" % (table_name, obj_name)
        try:
            cursor.execute(sql)
            data_list = cursor.fetchall()
            data_frame = pd.DataFrame(data_list, columns=["time", "confirm_sum", "cured_sum", "dead_sum", "name"])
            the_times = data_frame.groupby("time").sum()
            the_times['time'] = the_times.index
            the_times["name"] = None
            the_times = the_times.reindex(columns=["time", "confirm_sum", "cured_sum", "dead_sum", "name"])
            data_list = the_times.values.tolist()
            # print(data_list)
        except:
            conn.rollback()
            print("查询%s表失败" % table_name)

    elif type == 2:  # 城市级
        table_name = "china_city"
        sql = "select time,confirm_sum,cured_sum, dead_sum,name from %s where name ='%s'" % (table_name, obj_name)
        try:
            cursor.execute(sql)
            data_list = cursor.fetchall()
        except:
            conn.rollback()
            print("查询%s表失败" % table_name)

    return list(data_list[-7:])


def SIR(sir, t, beta, gamma):
    "SIR模型的微分方程"
    S, I, R = sir
    dsdt = - beta * S * I
    didt = beta * S * I - gamma * I
    drdt = gamma * I
    return [dsdt, didt, drdt]


def f(beta, gamma):
    # 求解时序变化
    corr = []
    for a, b in zip(beta, gamma):
        result = odeint(SIR, [S0, I0, R0], t, args=(a, b))
        St, It, Rt = result[:, 0], result[:, 1], result[:, 2]
        corr.append(np.mean((It - data) ** 2))
    return np.array(corr)


def best_solve():
    # 定义粒子个数
    N = 20
    # 定义惯性因子
    w = 0.1
    # 定义C1，C2
    c1, c2 = 2, 2
    # 初始化位置
    x = np.random.uniform(0, 1, [N, 2])
    x[:, 0] *= 0.04
    x[:, 1] *= 0.25
    # 初始化速度
    v = np.random.uniform(0, 1, [N, 2])
    v[:, 0] *= 0.04 * 0.03
    v[:, 1] *= 0.25 * 0.03
    # 个体最佳位置
    p_best = np.copy(x)

    fitness = f(x[:, 0], x[:, 1])
    fitness = np.expand_dims(fitness, 1)
    # 群体最佳位置
    g_best = p_best[np.argmin(fitness)]
    N_step = 100
    store = np.zeros([N, N_step, 2])
    for step in range(N_step):
        # 计算速度v
        store[:, step, :] = x
        r1, r2 = np.random.random([N, 1]), np.random.random([N, 1])
        v = w * v + (1 - w) * (c1 * r1 * (p_best - x) + c2 * r2 * (g_best - x))
        # 更新位置
        x = x + v
        x = np.clip(x, 0, 0.5)
        # 计算适应度
        fitness_new = f(x[:, 0], x[:, 1])
        fitness_new = np.expand_dims(fitness_new, 1)
        fit = np.concatenate([fitness, fitness_new], 1)
        fitness = fitness_new
        # 计算个体最优解
        p_best_for_sel = np.concatenate([
            np.expand_dims(x, 1),
            np.expand_dims(p_best, 1)], 1)
        p_best = p_best_for_sel[[i for i in range(N)], np.argmin(fit, 1), :]
        fit_p = f(p_best[:, 0], p_best[:, 1])
        # 计算全局最优解
        g_best = x[np.argmin(fitness[:, 0])]
    return g_best


def getTotalNum(conn, type, obj_name):
    cursor = conn.cursor()
    data_list = None
    if type == 0:  # 国家级
        table_name = "country_gps"
        sql = "select population from %s where name ='%s'" % (table_name, obj_name)
        try:
            cursor.execute(sql)
            data_list = cursor.fetchone()[0]
        except:
            conn.rollback()

    elif type == 1:  # 省级
        table_name = "china_province_gps"
        sql = "select population from %s where name ='%s'" % (table_name, obj_name)

        cursor.execute(sql)
        data_list = cursor.fetchone()[0]

    elif type == 2:  # 城市级
        table_name = "china_city_gps"
        sql = "select population from %s where name ='%s'" % (table_name, obj_name)
        try:
            cursor.execute(sql)
            data_list = cursor.fetchone()[0]
        except:
            conn.rollback()

    return data_list


if __name__ == '__main__':
    conn = pymysql.connect(host="127.0.0.1", port=3306, user="root", passwd="root658", db="covid19", charset="utf8")

    # 默认情况下
    type = 0
    obj_name = "中国"
    total_num = getTotalNum(conn, type, obj_name)

    ags = sys.argv
    if len(ags) == 3:
        type = int(ags[1])
        obj_name = ags[2]

    whole_data = fetch_data(conn, type, obj_name)

    conn.close()

    whole_data = pd.DataFrame(whole_data, columns=['time', 'confirm_sum', 'cured_sum', 'dead_sum', 'confirm_now'])
    whole_data['confirm_now'] = whole_data['confirm_sum'] - whole_data['cured_sum'] - whole_data['dead_sum']
    data = whole_data['confirm_now']

    I0 = whole_data['confirm_now'][0]
    R0 = whole_data['cured_sum'][0] + whole_data['dead_sum'][0]
    S0 = total_num - I0 - R0

    # 定义7天
    t = np.linspace(0, 6, 7)
    a, b = best_solve()
    dt = np.linspace(0, 29, 30)
    result = odeint(SIR, [S0, I0, R0], dt, args=(a, b))

    St = result[:, 0]  # 易感染人数预测
    It = result[:, 1]  # 患病人数预测
    Rt = result[:, 2]  # 治愈人数 + 死亡人数 总数预测

    start_time = whole_data["time"][0].strftime('%Y-%m-%d')
    date_list = [x.strftime('%Y-%m-%d') for x in list(pd.date_range(start=start_time, periods=30))]

    It = It.astype(int)
    dicts = dict(zip(date_list, It))
    for dic in dicts:
        print( dic + ":" + str(dicts[dic]))


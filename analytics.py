import mysql.connector
import pandas as pd
import matplotlib.pyplot as plt

# Connect to MySQL
con = mysql.connector.connect(
    host="localhost",
    port=3306,
    user="root",
    password="Momo@7978",
    database="mess_management"
)

print("\n===== MESS ANALYTICS DASHBOARD =====\n")

# ---------------------------
# 1️⃣ Monthly Revenue Graph
# ---------------------------
query1 = "SELECT month, total_amount FROM payments"
df1 = pd.read_sql(query1, con)

if not df1.empty:
    monthly_income = df1.groupby("month")["total_amount"].sum()

    print("Monthly Revenue:\n")
    print(monthly_income)

    plt.figure()
    monthly_income.plot(kind='bar')
    plt.title("Monthly Mess Revenue")
    plt.xlabel("Month")
    plt.ylabel("Revenue (₹)")
    plt.tight_layout()
    plt.show()
else:
    print("No payment data found.\n")


# ---------------------------
# 2️⃣ Most Regular Member
# ---------------------------
query2 = """
SELECT member_id, COUNT(*) AS total_days
FROM attendance
WHERE present = 1
GROUP BY member_id
ORDER BY total_days DESC
LIMIT 1
"""

df2 = pd.read_sql(query2, con)

if not df2.empty:
    print("\n🏆 Most Regular Member:")
    print(df2)
else:
    print("No attendance data found.\n")


# ---------------------------
# 3️⃣ Most Meal Consumption
# ---------------------------
query3 = """
SELECT member_id,
SUM(breakfast + lunch + dinner) AS total_meals
FROM attendance
GROUP BY member_id
ORDER BY total_meals DESC
LIMIT 1
"""

df3 = pd.read_sql(query3, con)

if not df3.empty:
    print("\n🍽 Most Meal Consumption:")
    print(df3)
else:
    print("No meal data found.\n")



query4 = """
SELECT 
SUM(breakfast) AS total_breakfast,
SUM(lunch) AS total_lunch,
SUM(dinner) AS total_dinner
FROM attendance
"""

df4 = pd.read_sql(query4, con)

if not df4.empty:
    values = df4.iloc[0]
    labels = ['Breakfast', 'Lunch', 'Dinner']

    plt.figure()
    plt.pie(values, labels=labels, autopct='%1.1f%%')
    plt.title("Meal Distribution")
    plt.show()

    con.close()
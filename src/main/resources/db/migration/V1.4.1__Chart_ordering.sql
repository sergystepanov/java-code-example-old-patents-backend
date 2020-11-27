CREATE or replace FORCE VIEW PROXY_SERVER_REQUEST_MONTH_CHART("PERIOD", "REQUESTS") AS
SELECT FORMATDATETIME("ACCESS", 'Y-MM') AS "PERIOD",
       SUM("COUNT")                     AS "REQUESTS"
FROM (SELECT * FROM PROXY_SERVER_REQUEST ORDER BY ACCESS)
GROUP BY FORMATDATETIME("ACCESS", 'Y-MM');
package com.sichuan.sichuanproject.schedule;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * @author
 */

@Data
public class RegressionLine {
    /** sum of x */
    private double sumX;

    /** sum of y */
    private double sumY;

    /** sum of x*x */
    private double sumXX;

    /** sum of x*y */
    private double sumXY;

    /** sum of y*y */
    private double sumYY;

    /** sum of yi-y */
    private double sumDeltaY;

    /** sum of sumDeltaY^2 */
    private double sumDeltaY2;

    /** 误差 */
    private double sse;

    private double sst;

    private double E;

    private String[] xy;

    private ArrayList listX;

    private ArrayList listY;

    private int XMin, XMax, YMin, YMax;

    private float a0;

    private float a1;

    private int pn;

    private boolean coefsValid;

    public RegressionLine() {
        XMax = 0;
        YMax = 0;
        pn = 0;
        xy = new String[2];
        listX = new ArrayList();
        listY = new ArrayList();
    }

    public RegressionLine(DataPoint data[]) {
        pn = 0;
        xy = new String[2];
        listX = new ArrayList();
        listY = new ArrayList();
        for (int i = 0; i < data.length; ++i) {
            addDataPoint(data[i]);
        }
    }

    /**
     * 计算y=a0+a1*x 中的a0
     *
     * @return
     */
    public float getA0() {
        validateCoefficients();
        return a0;
    }

    /**
     * 计算y=a0+a1*x中的a1
     *
     * @return
     */
    public float getA1() {
        validateCoefficients();
        return a1;
    }


    public void addDataPoint(DataPoint dataPoint) {
        sumX += dataPoint.x;
        sumY += dataPoint.y;
        sumXX += dataPoint.x * dataPoint.x;
        sumXY += dataPoint.x * dataPoint.y;
        sumYY += dataPoint.y * dataPoint.y;

        if (dataPoint.x > XMax) {
            XMax = (int) dataPoint.x;
        }
        if (dataPoint.y > YMax) {
            YMax = (int) dataPoint.y;
        }

        // 把每个点的具体坐标存入ArrayList中，备用

        xy[0] = (int) dataPoint.x + "";
        xy[1] = (int) dataPoint.y + "";
        if (dataPoint.x != 0 && dataPoint.y != 0) {
            System.out.print(xy[0] + ",");
            System.out.println(xy[1]);

            try {
                listX.add(pn, xy[0]);
                listY.add(pn, xy[1]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ++pn;
        coefsValid = false;
    }

    /**
     * 计算a0+a1*x
     *
     * @param x
     * @return
     */
    public float func(int x) {
        if (pn < 2) {
            return Float.NaN;
        }

        validateCoefficients();
        return a0 + a1 * x;
    }

    public void reset() {
        pn = 0;
        sumX = sumY = sumXX = sumXY = 0;
        coefsValid = false;
    }

    /**
     * 计算方程系数 y=a0+a1*x 中的a0和a0
     */
    private void validateCoefficients() {
        if (coefsValid){
            return;
        }

        if (pn >= 2) {
            float xBar = (float) sumX / pn;
            float yBar = (float) sumY / pn;

            a1 = (float) ((pn * sumXY - sumX * sumY) / (pn * sumXX - sumX
                    * sumX));
            a0 = yBar - a1 * xBar;
        } else {
            a0 = a1 = Float.NaN;
        }

        coefsValid = true;
    }

    /**
     * 返回误差
     */
    public double getR() {
        // 遍历这个list并计算分母
        for (int i = 0; i < pn - 1; i++) {
            float Yi = (float) Integer.parseInt(listY.get(i).toString());
            float Y = func(Integer.parseInt(listX.get(i).toString()));
            float deltaY = Yi - Y;
            float deltaY2 = deltaY * deltaY;

            sumDeltaY2 += deltaY2;

        }

        sst = sumYY - (sumY * sumY) / pn;
        E = 1 - sumDeltaY2 / sst;

        return round(E, 4);
    }


    /**
     * 用于实现精准的四舍五入
     *
     * @param v
     * @param scale
     * @return
     */
    public double round(double v, int scale) {

        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }

//        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal b = new BigDecimal(v);
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();

    }

}

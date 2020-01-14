package util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * JAVA版本的自动生成有规则的订单号(或编号) 生成的格式是: 200908010001 前面几位为当前的日期,后面五位为系统自增长类型的编号 原理:
 * 1.获取当前日期格式化值; 2.读取文件,上次编号的值+1最为当前此次编号的值 (新的一天会重新从1开始编号)
 *
 * @author syx
 */
public class OrderNoUtils {
    /**
     * 时间格式化
     */
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMdd");


    /**
     * 获取范围内int值
     *
     * @param
     * @return
     */
    public static int getRandomRange(int max, int min) {
        return (int) (Math.random() * (max - min) + min);
    }

    /**
     * 随机位数编号
     *
     * @return
     */
    public static String generateCode(int length) {
        String[] codePool = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
        //获取一个0~9的随机整数
        StringBuilder generateCode = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomNum = getRandomRange(0, 10);
            generateCode.append(codePool[randomNum]);
        }
        return generateCode.toString();
    }

    /**
     * 生成账号
     *
     * @return
     */
    public static String getExNo() {
        return SDF.format(new Date()).substring(2, 8) + generateCode(5);
    }

    /**
     * 产生唯一 的序列号。
     *
     * @return 序列号
     */
    public static String getSerialNumber() {
        int hashCode = UUID.randomUUID().toString().hashCode();
        if (hashCode < 0) {
            hashCode = -hashCode;
        }
        synchronized (OrderNoUtils.class) {
            return SDF.format(new Date()).substring(2, 6) + String.format("%010d", hashCode);
        }
    }

    /**
     * 生成充值订单号
     *
     * @return 订单号
     */
    public static String getRechargeOrderNo() {
        //联动优势支付 订单前缀
        String prefix = "UM";
        int hashCode = UUID.randomUUID().toString().hashCode();
        if (hashCode < 0) {
            hashCode = -hashCode;
        }
        synchronized (OrderNoUtils.class) {
            return prefix + SDF.format(new Date()).substring(2, 8) + String.format("%010d", hashCode);
        }
    }

    /**
     * 生成支付宝充值订单号
     *
     * @return 订单号
     */
    public static String getALRechargeOrderNo() {
        //支付宝充值订单号
        String prefix = "AL";
        int hashCode = UUID.randomUUID().toString().hashCode();
        if (hashCode < 0) {
            hashCode = -hashCode;
        }
        synchronized (OrderNoUtils.class) {
            return prefix + SDF.format(new Date()).substring(2, 8) + String.format("%010d", hashCode);
        }
    }

    /**
     * 生成易通充值订单号
     *
     * @return 订单号
     */
    public static String getETRechargeOrderNo() {
        //生成易通充值订单号
        String prefix = "ET";
        int hashCode = UUID.randomUUID().toString().hashCode();
        if (hashCode < 0) {
            hashCode = -hashCode;
        }
        synchronized (OrderNoUtils.class) {
            return prefix + SDF.format(new Date()).substring(2, 8) + String.format("%010d", hashCode);
        }
    }

    /**
     * 生成华融充值订单号
     *
     * @return 订单号
     */
    public static String getHRRechargeOrderNo() {
        //生成华融充值订单号
        String prefix = "HR";
        int hashCode = UUID.randomUUID().toString().hashCode();
        if (hashCode < 0) {
            hashCode = -hashCode;
        }
        synchronized (OrderNoUtils.class) {
            return prefix + SDF.format(new Date()).substring(2, 8) + String.format("%010d", hashCode);
        }
    }

    /**
     * 生成华融提现订单号
     *
     * @return 订单号
     */
    public static String getHRCashOrderNo() {
        //生成华融提现订单号
        String prefix = "HRDF";
        int hashCode = UUID.randomUUID().toString().hashCode();
        if (hashCode < 0) {
            hashCode = -hashCode;
        }
        synchronized (OrderNoUtils.class) {
            return prefix + SDF.format(new Date()).substring(2, 8) + String.format("%010d", hashCode);
        }
    }

    /**
     * 生成连连充值订单号
     *
     * @return 订单号
     */
    public static String getLLRechargeOrderNo() {
        //生成连连充值订单号
        String prefix = "LL";
        int hashCode = UUID.randomUUID().toString().hashCode();
        if (hashCode < 0) {
            hashCode = -hashCode;
        }
        synchronized (OrderNoUtils.class) {
            return prefix + SDF.format(new Date()).substring(2, 8) + String.format("%010d", hashCode);
        }
    }

    /**
     * 生成九派充值订单号
     *
     * @return 订单号
     */
    public static String getNPRechargeOrderNo() {
        //生成九派充值订单号
        String prefix = "NP";
        int hashCode = UUID.randomUUID().toString().hashCode();
        if (hashCode < 0) {
            hashCode = -hashCode;
        }
        synchronized (OrderNoUtils.class) {
            return prefix + SDF.format(new Date()).substring(2, 8) + String.format("%010d", hashCode);
        }
    }

    /**
     * 生成提现订单号
     *
     * @return 订单号
     */
    public static String getCashOrderNo() {
        //生成提现订单号
        String prefix = "UM_TX";
        int hashCode = UUID.randomUUID().toString().hashCode();
        if (hashCode < 0) {
            hashCode = -hashCode;
        }
        synchronized (OrderNoUtils.class) {
            return prefix + SDF.format(new Date()).substring(2, 8) + String.format("%010d", hashCode);
        }
    }

    /**
     * 生成主委托订单号
     *
     * @return 订单号
     */
    public static String getMainDelegateOrderNo() {
        //生成主委托订单号
        String prefix = "m";
        int hashCode = UUID.randomUUID().toString().hashCode();
        if (hashCode < 0) {
            hashCode = -hashCode;
        }
        synchronized (OrderNoUtils.class) {
            return prefix + SDF.format(new Date()).substring(4, 8) + String.format("%010d", hashCode);
        }
    }


    /**
     * 生成子委托订单号
     *
     * @return 订单号
     */
    public static String getSubDelegateBillNo() {
        //生成子委托订单号
        String prefix = "s";
        int hashCode = UUID.randomUUID().toString().hashCode();
        if (hashCode < 0) {
            hashCode = -hashCode;
        }
        synchronized (OrderNoUtils.class) {
            return prefix + SDF.format(new Date()).substring(4, 8) + String.format("%010d", hashCode);
        }
    }
}

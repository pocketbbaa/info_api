package com.kg.platform.common.similarity.utils;

import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;
import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SimHashUtil {

    private static final int hashbits = 64;

    /**
     * 计算相似度
     *
     * @param distance
     * @return
     */
    public static double getSemblance(int distance) {
        return CalculateUtils.sub(1d, CalculateUtils.div((double) distance, 35d, 4));
    }


    /**
     * 计算海明距离,海明距离越小说明越相似
     * 对于短文本，小于3相识度效果不好
     *
     * @param hash1
     * @param hash2
     * @return
     */
    public static int hammingDistance(BigInteger hash1, BigInteger hash2) {
        BigInteger m = new BigInteger("1").shiftLeft(hashbits).subtract(
                new BigInteger("1"));
        BigInteger x = hash1.xor(hash2).and(m);
        int tot = 0;
        while (x.signum() != 0) {
            tot += 1;
            x = x.and(x.subtract(new BigInteger("1")));
        }
        return tot;
    }


    /**
     * 获取文本simHash
     *
     * @return
     */
    public static BigInteger simHash(String tokens) {

        tokens = cleanResume(tokens); // cleanResume 删除一些特殊字符
        int[] v = new int[hashbits];
        List<Term> termList = StandardTokenizer.segment(tokens); // 对字符串进行分词
        //对分词的一些特殊处理 : 比如: 根据词性添加权重 , 过滤掉标点符号 , 过滤超频词汇等;
        Map<String, Integer> weightOfNature = new HashMap<String, Integer>(); // 词性的权重
        weightOfNature.put("n", 2); //给名词的权重是2;
        Map<String, String> stopNatures = new HashMap<String, String>();//停用的词性 如一些标点符号之类的;
        stopNatures.put("w", ""); //
        int overCount = 5; //设定超频词汇的界限 ;
        Map<String, Integer> wordCount = new HashMap<String, Integer>();

        for (Term term : termList) {
            String word = term.word; //分词字符串

            String nature = term.nature.toString(); // 分词属性;
            //  过滤超频词
            if (wordCount.containsKey(word)) {
                int count = wordCount.get(word);
                if (count > overCount) {
                    continue;
                }
                wordCount.put(word, count + 1);
            } else {
                wordCount.put(word, 1);
            }

            // 过滤停用词性
            if (stopNatures.containsKey(nature)) {
                continue;
            }

            // 2、将每一个分词hash为一组固定长度的数列.比如 64bit 的一个整数.
            BigInteger t = hash(word);
            for (int i = 0; i < hashbits; i++) {
                BigInteger bitmask = new BigInteger("1").shiftLeft(i);
                // 3、建立一个长度为64的整数数组(假设要生成64位的数字指纹,也可以是其它数字),
                // 对每一个分词hash后的数列进行判断,如果是1000...1,那么数组的第一位和末尾一位加1,
                // 中间的62位减一,也就是说,逢1加1,逢0减1.一直到把所有的分词hash数列全部判断完毕.
                int weight = 1;  //添加权重
                if (weightOfNature.containsKey(nature)) {
                    weight = weightOfNature.get(nature);
                }
                if (t.and(bitmask).signum() != 0) {
                    // 这里是计算整个文档的所有特征的向量和
                    v[i] += weight;
                } else {
                    v[i] -= weight;
                }
            }
        }
        BigInteger fingerprint = new BigInteger("0");
        for (int i = 0; i < hashbits; i++) {
            if (v[i] >= 0) {
                fingerprint = fingerprint.add(new BigInteger("1").shiftLeft(i));
            }
        }
        return fingerprint;
    }

    /**
     * 获取百分比文本
     *
     * @param result
     * @return
     */
    public static String getPercent(double result) {
        return result * 100 + "%";
    }


    /**
     * 清除html标签
     *
     * @param content
     * @return
     */
    private static String cleanResume(String content) {
        if (StringUtils.isEmpty(content)) {
            return content;
        }
        return content.replaceAll("</?[a-zA-Z]+[^><]*>", "");
    }


    /**
     * 对单个的分词进行hash计算;
     *
     * @param source
     * @return
     */
    private static BigInteger hash(String source) {
        if (source == null || source.length() == 0) {
            return new BigInteger("0");
        } else {
            //当sourece 的长度过短，会导致hash算法失效，因此需要对过短的词补偿
            StringBuilder sourceBuilder = new StringBuilder(source);
            while (sourceBuilder.length() < 3) {
                sourceBuilder.append(sourceBuilder.charAt(0));
            }
            source = sourceBuilder.toString();
            char[] sourceArray = source.toCharArray();
            BigInteger x = BigInteger.valueOf(((long) sourceArray[0]) << 7);
            BigInteger m = new BigInteger("1000003");
            BigInteger mask = new BigInteger("2").pow(hashbits).subtract(new BigInteger("1"));
            for (char item : sourceArray) {
                BigInteger temp = BigInteger.valueOf((long) item);
                x = x.multiply(m).xor(temp).and(mask);
            }
            x = x.xor(new BigInteger(String.valueOf(source.length())));
            if (x.equals(new BigInteger("-1"))) {
                x = new BigInteger("-2");
            }
            return x;
        }
    }

    public static void main(String[] args) {
        System.out.println(new BigInteger("18081720030363125979"));
    }

}

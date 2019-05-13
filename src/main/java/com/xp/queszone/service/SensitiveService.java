package com.xp.queszone.service;

import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Service
public class SensitiveService implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveService.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWords.txt");
            InputStreamReader reader = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String lineText;
            while((lineText = bufferedReader.readLine()) != null) {
                addKeyWord(lineText.trim());
            }
            bufferedReader.close();
        }catch (Exception e) {
            logger.error("读取敏感文件失败"+e.getMessage());
        }
    }

    //判断是否是一个符号
    private boolean isSymbol(char c) {
        int ic = (int) c;
        // 0x2E80-0x9FFF 东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic > 0x9FFF);
    }

    //敏感词过滤
    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return text;
        }
        StringBuilder result = new StringBuilder();
        String replacement = "***";
        TrieNode tempNode = rootNode;
        int begin = 0;
        int position = 0;
        while(position < text.length()) {
            char c = text.charAt(position);
            if (isSymbol(c)) {
                if (tempNode == rootNode) {
                    result.append(c);
                    ++begin;
                }
                ++position;
                continue;
            }
            tempNode = tempNode.getSubNode(c);
            if (null == tempNode) {
                result.append(text.charAt(begin));
                position++;
                begin = position;
                tempNode = rootNode;
            } else if (tempNode.isKeywordEnd()) {
                result.append(replacement);
                position++;
                begin = position;
            } else {
                ++position;
            }
        }
        result.append(text.substring(begin));
        return result.toString();
    }

    //根节点
    private TrieNode rootNode = new TrieNode();

    //往前缀树中增加过滤敏感词
    private void addKeyWord(String lineText) {
        TrieNode tempNode = rootNode;
        for (int i = 0; i < lineText.length(); ++i) {
            Character c = lineText.charAt(i);
            TrieNode node = tempNode.getSubNode(c);
            if (null == node) {
                node = new TrieNode();
                tempNode.addSubNode(c,node);
            }
            tempNode = node;

            if (lineText.length()-1 == i) {
                tempNode.setKeywordEnd(true);
            }
        }
    }


    //敏感词检测前缀树
    private class TrieNode {
        //是不是关键词的结尾
        private boolean end = false;

        //当前节点下面的所有子节点，如敏感词ab,ac,ad,那么a结点下就有b，c，d 3个节点
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        public void addSubNode(Character key, TrieNode node) {
            subNodes.put(key,node);
        }

        TrieNode getSubNode(Character key) {
            return subNodes.get(key);
        }

        boolean isKeywordEnd() {
            return end;
        }

        void setKeywordEnd(boolean end) {
            this.end = end;
        }

        public int getSubNodeCount() {
            return subNodes.size();
        }
    }
}

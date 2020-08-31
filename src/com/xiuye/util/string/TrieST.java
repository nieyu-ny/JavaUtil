package com.xiuye.util.string;

import com.xiuye.util.cls.XType;
import com.xiuye.util.log.XLog;

import java.util.LinkedList;
import java.util.Queue;

public class TrieST<V> {
    private static int R = 256;
    private Node root;

    private static class Node {
        private Object val;
        private Node[] next = new Node[R];
    }

    public V get(String key) {
        Node x = get(root, key, 0);
        if (x == null) return null;
        return (V) x.val;
    }

    //core
    private Node get(Node x, String key, int d) {
        //return back x as root
        if (x == null) {
            return null;
        }
        if (d == key.length()) {
            return x;
        }
        char c = key.charAt(d);
        return get(x.next[c], key, d + 1);
    }

//    private char charAt(int d) {
//        return 0;
//    }

    public void put(String key, V val) {
        root = put(root, key, val, 0);
    }

    //core
    private Node put(Node x, String key, V val, int i) {
        if (x == null) {//no node,alloc node!
            x = XType.newInstance(Node::new);
        }
        if (i == key.length()) {
            x.val = val;
            return x;
        }
        char c = key.charAt(i);
        x.next[c] = put(x.next[c], key, val, i + 1);
        return x;
    }

    public int size() {
        return size(root);
    }

    private int size(Node x) {
        if (x == null) {
            return 0;
        }
        int cnt = 0;
        if (x.val != null) {
            cnt++;
        }
        for (char c = 0; c < R; c++) {//travel all letters!
            cnt += size(x.next[c]);
        }
        return cnt;

    }


    public Iterable<String> keys() {
        return keysWithPrefix("");
    }

    private Iterable<String> keysWithPrefix(String pre) {
        Queue<String> q = XType.newInstance(LinkedList::new);
        collect(get(root, pre, 0), pre, q);
        return q;
    }

    private void collect(Node x, String pre, Queue<String> q) {
        if (x == null) return;
        if (x.val != null) {
            q.add(pre);
        }


        for (char c = 0; c < R; c++) {//travel all characters!
            //pre+c == key!!!
            collect(x.next[c], pre + c, q);
        }


    }

    public Iterable<String> keysThatMatch(String pat) {
        Queue<String> q = XType.newInstance(LinkedList::new);
        collect(root, "", pat, q);
        return q;
    }

    private void collect(Node x, String pre, String pat, Queue<String> q) {
        int d = pre.length();
        if (x == null) {
            return;
        }
        if (d == pat.length() && x.val != null) {
            q.add(pre);
        }
        if (d == pat.length()) {
            return;
        }

        char next = pat.charAt(d);
        for (char c = 0; c < R; c++) {
            if (next == '.' || next == c) {
                collect(x.next[c], pre + c, pat, q);
            }
        }
    }

    public String longestPreffixOf(String s) {
        int length = search(root,s,0,0);
        return s.substring(0,length);
    }

    private int search(Node x, String s, int d, int length) {
        if(x == null)return length;
        if(x.val != null)length=d;
        if(d == s.length())return length;
        char c = s.charAt(d);
        return search(x.next[c],s,d+1,length);
    }

    public void delete(String key){
        root = delete(root,key,0);
    }

    private Node delete(Node x, String key, int d) {
        if(x == null)return null;
        if(d == key.length()){
            x.val = null;
        }
        else{
            char c = key.charAt(d);
            x.next[c] = delete(x.next[c],key,d+1);
        }
        if(x.val != null)
        {
            return x;
        }
        for(char c=0;c<R;c++){
            if(x.next[c] != null){
                return x;
            }

        }
        return null;
    }


    public static void main(String[] args) {
        TrieST trieST = new TrieST();
        trieST.put("she", 123);

        XLog.ln(trieST.get("she"), trieST.size());
    }

}

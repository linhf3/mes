package com.ruoyi.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author lhf
 * @Title: Sort
 * @Description: 常见排序算法
 * @date 2021/9/2 11:19
 */
@Slf4j
public class SortUtils {

    /**
     * @author lhf
     * @Title: 冒泡排序
     * @date 2021/9/2 13:50
     */
    public static void bubbleSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - i - 1; j++) {
                if (arr[j] < arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    /**
     * @author lhf
     * @Title: 选择排序
     * @date 2021/9/2 14:19
     */
    public static void chooseSort(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            int temp = i;
            boolean flag = false;
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[temp] > arr[j]) {
                    temp = j;
                    flag = true;
                }
            }
            if (flag && i != temp) {
                int temps = arr[i];
                arr[i] = arr[temp];
                arr[temp] = temps;
            }
        }
    }

    /**
     * @author lhf
     * @Title: 插入排序
     * @date 2021/9/2 15:10
     */
    public static void insertSort(int[] arr) {
        int preIndex, current;
        for (int i = 1; i < arr.length; i++) {
            //前一个值索引
            preIndex = i - 1;
            //当前值
            current = arr[i];
            while (preIndex >= 0 && arr[preIndex] > current) {
                arr[preIndex + 1] = arr[preIndex];
                preIndex--;
            }
            arr[preIndex + 1] = current;
            //System.out.println("Arrays.toString(arr) = " + Arrays.toString(arr));
        }
    }

    /**
     * lhf
     * 冒泡排序
     * param Comparable[] arr
     */
    public static void bubbleSort(Comparable[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - i - 1; j++) {
                int result = arr[j].compareTo(arr[j + 1]);
                if (result > 0) {
                    Comparable temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    /**
     * lhf
     * 选择排序
     */
    public static void chooseSort(Comparable[] arr) {
        for (int i = 0; i < arr.length; i++) {
            int temp = i;
            boolean flag = false;
            for (int j = i + 1; j < arr.length; j++) {
                int result = arr[temp].compareTo(arr[j]);
                if (result > 0) {
                    temp = j;
                    flag = true;
                }
            }
            if (flag && i != temp) {
                Comparable temps = arr[i];
                arr[i] = arr[temp];
                arr[temp] = temps;
            }
        }
    }

    /**
     * lhf
     * 插入排序
     */
    public static void insertSort(Comparable[] arr) {
        int preIndex;
        Comparable current;
        for (int i = 1; i < arr.length; i++) {
            //前一个值索引
            preIndex = i - 1;
            //当前值
            current = arr[i];
            while (preIndex >= 0 && arr[preIndex].compareTo(current) > 0) {
                arr[preIndex + 1] = arr[preIndex];
                preIndex--;
            }
            arr[preIndex + 1] = current;
        }
    }

    /**
     * lhf
     * 插入排序(希尔排序) 交换式
     */
    public static void shellSort(int[] arr) {
        int temp = 0;
        //控制希尔增量 gap 分组、步长
        for (int gap=arr.length/2;gap>0;gap /= 2){
            for (int i= gap; i < arr.length; i++) {
                for (int j = i - gap; j >=0 ; j -= gap) {
                    if(arr[j]>arr[j+gap]){
                        temp = arr[j];
                        //先赋值arr[j]
                        arr[j] = arr[j+gap];
                        arr[j+gap] = temp;
                    }
                }
            }
        }
    }

    /**
     * lhf
     * 插入排序(希尔排序) 插入式
     */
    public static void shellInserSort(Integer[] arr) {
        int temp = 0;
        //控制希尔增量 gap 分组、步长
        for (int gap=arr.length/2;gap>0;gap /= 2){
            for (int i= gap; i < arr.length; i++) {
                int j = i;
                temp = arr[j];
                while (j-gap>=0 && temp< arr[j-gap]){
                    arr[j] = arr[j-gap];
                    j -= gap;
                }
                arr[j] = temp;
            }
        }
    }


    /**
     * lhf int [1,9,-8,3,9,-4,7,6]
     * 快速排序
     */
    public static  void quickSort(int[] arr, int left, int right){
        //记录左边下标
        int l = left;
        //记录右边下标
        int r = right;
        //中轴值
        int p = arr[(l+r)/2];
        //临时变量，记录交换值
        int temp = 0;
        //循环找比p小的值放在左边，比p大的值放在右边
        while ( l < r ){
            //在左边开始找,当数组小于p，不断循环，找到比中轴值大的值才退出，此时得到比p大的值的数组下标
            //最坏的结果是找到p的位置，停止循环
            while (arr[l] <p){
                l +=1;
            }
            //在右边开始找,当数组大于p，不断循环，找到比中轴值小的值才退出，此时得到比p小的值的数组下标
            //最坏的结果是找到p的位置，停止循环
            //System.out.println("arr[r] = " + arr[r]);
            while (arr[r] > p ){
                r -=1;
            }
            //说明中轴值左右两边已经满足左边比中轴值小，右边比中轴值大，但是还不是有序的
            if(l >= r){
                break;//无需交换直接退出
            }
            //左右两边进行交换
            temp = arr[l];
            arr[l] = arr[r];
            arr[r] = temp;
            //交换完成后，发现arr[l] == p，则r-1，前移，左递归时right=r
            if(arr[l] == p){
                r -= 1;
            }
            //交换完成后，发下arr[l] == p，则l+1，后移，递归右边时的left=l
            if(arr[r] == p){
                l += 1;
            }
            if(l == r ){
                r -= 1;
                l += 1;
            }
           if(left<r){
                quickSort(arr,left,r);
            }
            if(l<right){
                quickSort(arr,l,right);
            }

        }
    }


    /**
     * lhf
     * 二分查找，不考虑重复的情况，循环方式
     */
    public static int binarySearch(Integer[] arr, int left, int rigth, int searchNumber) {
        while (left <= rigth) {
            int mid = (left + rigth) / 2;
            int midValue = arr[mid];
            if (searchNumber == midValue) {
                return mid;
            } else if (searchNumber < midValue) {
                rigth = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return -1;
    }

    /**
     * lhf
     * 二分查找，不考虑重复的情况，递归方式
     */
    public static int searchByRecursive(Object[] arr, int left, int rigth, double searchNumber) {
        if (left > rigth) {
            return -1;
        }
        int mid = (left + rigth) / 2;
        double midValue = (double) arr[mid];
        if (searchNumber == midValue) {
            return mid;
        } else if (searchNumber > midValue) {
            left = mid + 1;
            return searchByRecursive(arr, left, rigth, searchNumber);
        } else {
            rigth = mid - 1;
            return searchByRecursive(arr, left, rigth, searchNumber);
        }
    }

    /**
     * lhf
     * 二分查找，不考虑重复的情况，递归方式
     */
   /* public static int searchByRecursive(int[] arr, int left, int rigth, int searchNumber) {
        if (left > rigth) {
            return -1;
        }
        int mid = (left + rigth) / 2;
        int midValue = arr[mid];
        if (searchNumber == midValue) {
            return mid;
        } else if (searchNumber > midValue) {
            left = mid + 1;
            return searchByRecursive(arr, left, rigth, searchNumber);
        } else {
            rigth = mid - 1;
            return searchByRecursive(arr, left, rigth, searchNumber);
        }
    }*/

    /**
     * lhf
     * 二分查找，考虑重复的情况，循环方式
     * @return
     */
    public static List binarySearchs(int[] arr, int left, int rigth, int searchNumber) {
        List<Integer> list = new ArrayList();
        while (left <= rigth) {
            int mid = (left + rigth) / 2;
            int midValue = arr[mid];
            if (searchNumber == midValue) {
                list.add(mid);
                int preN = mid-1;
                int next = mid +1;
                //同时开始向左向右扫描
                while (true){
                    if(preN<0 && next >arr.length-1){
                        break;
                    }
                    if(preN>=0){
                        if(searchNumber != arr[preN]){
                            preN = -1;
                        }else {
                            list.add(preN);
                            preN--;
                        }
                    }
                    if(next<=arr.length-1){
                        if(searchNumber != arr[next]){
                            next = arr.length;
                        }else {
                            list.add(next);
                            next++;
                        }
                    }
                }
                Collections.sort(list);
                return list;
            } else if (searchNumber < midValue) {
                rigth = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return list;
    }

/*    public static  void quickSort(int[] arr, int left, int right){

        if (left >= right) {    // 必须加
            return;
        }

        int temp = arr[left]; // 以左边的元素为基准元素
        int i = left, j = right; // i,j为两个游标
        while (i < j) {
            while (i < j && arr[j] >= temp){ // 右边先走
                j--;
            }
            while (i < j && arr[i] <= temp) {
                i++;
            }
            if (i < j) {
                swap(arr, i, j);
            }
        }
        arr[left] = arr[i]; // 注意，这一步必须要，填上最左边的坑
        arr[i] = temp; // 基准元素就位
        quickSort(arr, left, i - 1);    // 递归操作左边部分
        quickSort(arr, i + 1, right);   // 递归操作右边部分
    }
    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }*/



}

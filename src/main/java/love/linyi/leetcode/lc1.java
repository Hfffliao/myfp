package love.linyi.leetcode;

import java.util.*;

public class lc1{
    public static void main(String[] args) {
        Map<String,Integer> map=new HashMap<>();
        map.put("001",50);
        map.put("002",40);
        map.put("003",55);
        map.put("004",60);
        map.put("005",90);
        map.put("006",80);
        map.put("007",100);
        Set<String> nameSet =map.keySet();
        List<String> name=new ArrayList<>();
        List<Integer> score=new ArrayList<>();
        for (String n:nameSet){
            name.add(n);
            score.add(map.get(n));
        }
        for (int i=score.size();i>1;i--){
            for (int j=0;j<i-1;j++){
                if (score.get(j)<score.get(j+1)){
                    Collections.swap(score,j,j+1);
                    Collections.swap(name,j,j+1);
                }

            }
        }
        System.out.println("前三");
        for (int i=0;i<3;i++){
            System.out.println("姓名："+name.get(i)+"成绩："+score.get(i));
        }
    }

}

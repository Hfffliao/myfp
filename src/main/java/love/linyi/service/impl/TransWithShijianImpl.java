package love.linyi.service.impl;

import love.linyi.service.TransWithShijian;

public class TransWithShijianImpl implements TransWithShijian {
   /* String start; String date; String stop;
    String starth; String datem; String stoph;
    String startm; String dated; String stopm;*/
    String[] strings=new  String[9];//前六个用来存分配的
    private void geshi(){
        for (int i=0;i<6;i++){
            if(strings[i]!=null){
                if(strings[i].length()==1){
                    strings[i] = "0"+strings[i];
                }
            }

        }
    }
    private void fenpei(){
        for (int i=6;i<9;i++){
            int space=strings[i].indexOf(' ');
            if (space != -1) {
                // 提取空格前的部分（不包含空格）
                 strings[i-6]= strings[i].substring(0, space);
                // 提取空格后的部分（跳过空格）
                strings[i-3] = strings[i].substring(space + 1);
                System.out.println("前半部分: " + strings[i-6]); // 输出 "Hello"
                System.out.println("后半部分: " + strings[i-3]); // 输出 "World"
            } else {
                if(i==7){
                    strings[i-3]= strings[i];
                    strings[i-6] = "04";
                    System.out.println(strings[i-3]);
                    System.out.println(strings[i-6]);
                }else {
                    strings[i-6]= strings[i];
                    strings[i-3] = "00";
                }

            }
        }
    }
    public String[] tran(String start, String date, String stop){//向外提供服务
        strings[6] = start;
        strings[7] = date;
        strings[8] = stop;
        fenpei();
        geshi();
        String[] strings1 =new String[2];
        strings1[0]="2025-"+strings[1]+"-"+strings[4]+" "+strings[0]+":"+strings[3]+":00";
        strings1[1]="2025-"+strings[1]+"-"+strings[4]+" "+strings[2]+":"+strings[5]+":00";
        return strings1;
    }
}

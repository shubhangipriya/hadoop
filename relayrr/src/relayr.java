import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class relayr {

    List<String > faculty2List = new ArrayList<>();

    public relayr(String faculty[]) {

//        for (int i = 0; i < faculty.length; i++) {
//            System.out.println(faculty[i]);
//        }
        this.faculty2List = Arrays.asList(faculty);
    }

    public List< String> find(String z) {

        int has[] = new int[26];
        int has2[] = new int[26];
        for(int i=0;i<26;i++)
        {
            has[i]=0;
        }
        int y = z.length();

        for(int i=0;i<y;i++)
        {
            has[z.charAt(i)-'a']++;
        }

        List< String> yy = new ArrayList<>();
        boolean flg=false;

        int faculty2ListSize = faculty2List.size();
        for(int i=0;i<faculty2ListSize;i++) {
            String cc = faculty2List.get(i);
            flg=false;

            for(int j=0;j<26;j++)
            {
                has2[j]=0;
            }
            for(int j=0;j<cc.length();j++)
            {
                has2[cc.charAt(j)-'a']++;
            }

            for(int j=0;j<26;j++)
            {
                if(has[j]==has2[j]) {

                }
                else{
                    flg= true;
                    break;
                }
            }

            if(flg == false) {
                yy.add(cc);
            }

        }

        return  yy;
    }



}

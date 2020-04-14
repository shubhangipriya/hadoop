import java.io.Serializable;
import java.util.List;

//assumption thisIsAStringArray is of size 6
//the size can be made fix or variable by pre-defining it or we can
//use array list inorder to make it variable size

class model {

    public static void main(String args[])
    {
        String[] thisIsAStringArray = new String[6];
        thisIsAStringArray[0] = "asd";
        thisIsAStringArray[1] = "asdd";
        thisIsAStringArray[2] = "fre";
        thisIsAStringArray[3] = "glk";
        thisIsAStringArray[4] = "lkm";
        thisIsAStringArray[5] = "das";
        relayr p1 = new relayr(thisIsAStringArray);

        List< String> yy = p1.find("sad");

        if(yy.size()>0) {

            for (int h = 0; h < yy.size(); h++) {
                System.out.println(yy.get(h));
            }
        }

    }


}

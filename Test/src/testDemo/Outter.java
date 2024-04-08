package testDemo;

public class Outter {

    private static String outterName = "Outter";
    private String outterAge = "12";

    public class Inner {

        private String InnerName = "Inner";

        public void setName(String name) {

            this.InnerName = name;
        }
    }


}

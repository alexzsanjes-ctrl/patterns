
public class Main {
    public static void main(String[] args) {
        DataGenerator.UserInfo User = DataGenerator.Registration.generateUser("ru");
        String city = User.getCity();
        String name = User.getName();
        String phone = User.getPhone();
        String Date = DataGenerator.generateDate(4);
        System.out.println(User);
        System.out.println(city+","+name+","+phone);
        System.out.println(Date);
    }
}

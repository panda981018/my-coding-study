package toy;

public class LetsGo {

    public static void main(String[] args) {
        Human jiwon = new Human();
        Kitty giKim = new Kitty("깡이");

        System.out.println(jiwon.call(giKim));
        System.out.println(jiwon.play(giKim));
    }
}

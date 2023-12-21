package toy;

public class Human {

    public String play(Animal animal) {
        return "??? : " + animal.action();
    }

    public String call(Animal animal) {
        return "주인 : 인누와!! 울" + animal.getName();
    }
}

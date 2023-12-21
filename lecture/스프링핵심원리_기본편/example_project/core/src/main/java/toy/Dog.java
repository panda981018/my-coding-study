package toy;

import java.util.Arrays;
import java.util.List;

public class Dog implements Animal {

    private String name;

    public Dog(String name) {
        this.name = name;
    }

    private List<String> actions = Arrays.asList("왕왕!!", "냅다 도망", "(집에 안간다고 버틴다)");

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String action() {
        int random = (int) (Math.random() * actions.size());
        return actions.get(random);
    }
}

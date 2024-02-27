package toy;

import java.util.Arrays;
import java.util.List;

public class Kitty implements Animal {

    private String name;
    private final List<String> actions = Arrays.asList("야오옹~!!", "할퀸다", "품으로 들어간다");

    public Kitty(String name) {
        this.name = name;
    }

    public String addAction(String action) {
        actions.add(action);
        return "추가가 완료되었습니다.";
    }

    public String reduceAction(String action) {
        if (actions.contains(action)) {
            actions.remove(action);
            return "삭제가 완료되었습니다.";
        } else {
            return "그러한 행동은 존재하지 않습니다.";
        }
    }

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

package io.lindstrom.mp4;

import java.util.List;
import java.util.Optional;

public class BasicContainer implements Container {
    protected final List<Box> boxes;

    public BasicContainer(List<Box> boxes) {
        this.boxes = boxes;
    }

    public List<Box> getBoxes() {
        return boxes;
    }


    public <T extends Box> Optional<T> findBox(Class<T> clazz) {
        return findBox(boxes, clazz);
    }

    private static  <T extends Box> Optional<T> findBox(List<Box> boxes, Class<T> clazz) {
        for (Box box : boxes) {
            if (clazz.isInstance(box)) {
                return Optional.of(clazz.cast(box));
            } else if (box instanceof BasicContainer) {
                Optional<T> boxInContainer = findBox(((BasicContainer) box).getBoxes(), clazz);
                if (boxInContainer.isPresent()) {
                    return boxInContainer;
                }
            }
        }
        return Optional.empty();
    }


    @Override
    public String toString() {
        return "BasicContainer{" +
                "boxes=" + boxes +
                '}';
    }
}

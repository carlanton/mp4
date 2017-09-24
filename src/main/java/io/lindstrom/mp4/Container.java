package io.lindstrom.mp4;

import io.lindstrom.mp4.box.Box;

import java.util.List;
import java.util.Optional;

public class Container {
    protected final List<Box> boxes;

    public Container(List<Box> boxes) {
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
            } else if (box instanceof Container) {
                Optional<T> boxInContainer = findBox(((Container) box).getBoxes(), clazz);
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

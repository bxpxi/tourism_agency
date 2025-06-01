package ro.mpp.utils;

import java.util.ArrayList;

public interface IStringifiable {
    default String encode() {
        var classType = this.getClass();
        var fields = Reflection.getAllFields(classType);

        var propsStr = new ArrayList<String>();

        for (var f : fields) {
            f.setAccessible(true);
            try {
                String value = Stringifier.encode(f.get(this));
                propsStr.add(f.getName() + "=" + value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        String result = String.join(";", propsStr);

        return classType.getSimpleName() + "{" + result + "}";
    }
}

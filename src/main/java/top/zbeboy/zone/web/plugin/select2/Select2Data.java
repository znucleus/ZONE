package top.zbeboy.zone.web.plugin.select2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Select2Data {

    private List<Data> result = new ArrayList<>();

    public static Select2Data of() {
        return new Select2Data();
    }

    public List<Data> add(String id, String text) {
        this.result.add(new Data(id, text));
        return this.result;
    }

    public List<Data> getResult() {
        return result;
    }

    public Map<String, Object> send(boolean isMore) {
        Map<String, Object> map = new HashMap<>();
        map.put("results", result);
        map.put("pagination", isMore);
        return map;
    }

    class Data {
        private String id;

        private String text;

        Data(String id, String text) {
            this.id = id;
            this.text = text;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}

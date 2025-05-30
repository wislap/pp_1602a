package kulib;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

// 自定义反序列器用于解析PP类
public class PpMapDeserializer extends JsonDeserializer<Map<String, Double>> {

    @Override
    public Map<String, Double> deserialize(JsonParser parser, DeserializationContext ctxt)
            throws IOException {
        
        Map<String, Double> ppMap = new HashMap<>();
        JsonNode node = parser.getCodec().readTree(parser);
        
        Iterator<Map.Entry<String, JsonNode>> fields = node.fields();

        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            String key = entry.getKey();
            JsonNode value = entry.getValue();

            // 只保留数值字段
            if (value.isNumber()) {
                ppMap.put(key, value.doubleValue());
            }
        }

        return ppMap;
    }
}

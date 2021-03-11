import com.google.gson.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;

public class FlattenJson {

    public static void main(String[] args) {
        JsonObject jsonObject = parseInput();
        if (jsonObject.keySet().isEmpty()) {
            System.out.println("{}");
            return;
        }
        JsonObject flattenedJsonObject = new JsonObject();
        flattenJson(jsonObject, null, flattenedJsonObject);
        System.out.println(prettifyJson(flattenedJsonObject));
    }

    /**
     * Parses a string from System.in into a JsonObject
     */
    private static JsonObject parseInput() {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder jsonStb = new StringBuilder();
        String line;
        try {
            while ((line = input.readLine()) != null) {
                jsonStb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JsonParser.parseString(jsonStb.toString()).getAsJsonObject();
    }

    /**
     * Traverses the JsonObject inorder to build flattened keys, when a leaf/jsonPrimitive is found, the currentKey
     * along with its value is added to the flattenedJsonObject.
     * A prerequisite is that the traversedJsonObject only contains JsonPrimitives or JsonObjects.
     *
     * @param traversedJsonObject the current JsonObject that is being investigated.
     * @param currentKey          flattened key containing the path which is taken in the JsonObject
     * @param flattenedJsonObject JsonObject containing the flattened keys and corresponding values.
     */
    private static void flattenJson(JsonObject traversedJsonObject, String currentKey, JsonObject flattenedJsonObject) {
        Set<String> keys = traversedJsonObject.keySet();
        if (keys.isEmpty()) {
            flattenedJsonObject.add(currentKey, new JsonObject());
            return;
        }
        for (String key : keys) {
            JsonElement element = traversedJsonObject.get(key);
            if (element.isJsonObject()) {
                flattenJson((JsonObject) element, concatKeys(currentKey, key), flattenedJsonObject);
            } else if (element.isJsonPrimitive()) {
                flattenedJsonObject.add(concatKeys(currentKey, key), element);
            }
        }
    }

    /**
     * Helper function to concatenate key. Also deals with the edge case when flattenJson is in the Root-JsonObject and
     * no currentKey is given.
     */
    private static String concatKeys(String k1, String k2) {
        if (k1 == null) {
            return k2;
        }
        return k1.concat(".").concat(k2);
    }

    /**
     * Helper function to format the flattened JsonObject
     */
    public static String prettifyJson(JsonObject flattenedJsonObject) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(flattenedJsonObject);
    }
}

package es.ing.aoc.y2015;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import es.ing.aoc.common.Day;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day12 extends Day {

  @Override
  protected String part1(String fileContents) throws Exception {
    String doc = fileContents.split(System.lineSeparator())[0];
    Pattern pattern = Pattern.compile("(-?[0-9]+)");
    Matcher matcher = pattern.matcher(doc);
    int total = 0;
    while (matcher.find()) {
      total += Integer.parseInt(matcher.group(1));
    }
    return String.valueOf(total);
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    String doc = fileContents.split(System.lineSeparator())[0];
    JsonElement json = new Gson().fromJson(doc, JsonElement.class);
    int total = traverse(json);
    return String.valueOf(total);
  }

  private int traverse(JsonElement element) {
    if (element.isJsonPrimitive()) {
      JsonPrimitive primitive = element.getAsJsonPrimitive();
      if (primitive!=null && primitive.isNumber()) {
        return primitive.getAsInt();
      }
    } else if (element.isJsonArray()) {
      return element.getAsJsonArray().asList().stream().mapToInt(this::traverse).sum();
    } else {
      JsonObject obj = element.getAsJsonObject();
      boolean redFlag = obj.entrySet().stream()
          .map(Map.Entry::getValue)
          .filter(JsonElement::isJsonPrimitive)
          .anyMatch(value -> value.getAsString().equals("red"));

      if (!redFlag) {
        return obj.entrySet().stream().map(Map.Entry::getValue).mapToInt(this::traverse).sum();
      }
    }
    return 0;
  }

  public static void main(String[] args) {
    Day.run(Day12::new, "2015/D12_small.txt", "2015/D12_full.txt");
  }
}
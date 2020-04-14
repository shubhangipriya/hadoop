import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

// Program to clone a Map in Java
class Main
{
	public static<K,V> Map<K,V> clone(Map<K,V> original) {
		return original.entrySet()
						.stream()
						.collect(Collectors.toMap(Map.Entry::getKey,
												Map.Entry::getValue));
	}

	public static void main(String[] args) {
		Map<String, Integer> hashMap = new HashMap();
		hashMap.put("A", 1);
		hashMap.put("B", 2);
		hashMap.put("C", 3);

		Map<String, Integer> copy = clone(hashMap);
		System.out.println(copy);
	}
}
Usage

```java
import com.digiturtle.dxml.DReader;
import com.digiturtle.dxml.DTag;

DReader reader = new DReader(new FileInputStream("basic.xml"));
DTag languageArray = reader.getParents().get(0);
DTag english = languageArray.get("name", "EN");
DTag helloTranslated = english.get("name", "Hello");
String translation = helloTranslated.getContent();
```
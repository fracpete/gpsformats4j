# gpsformats4j

GPS format conversions in Java.

Very simple, very hacky. ;-) The main reason was to be able to convert
TCX files exported from a proprietary app into GPX ones so the 
[OsmAnd~](https://fossdroid.com/a/osmand~.html) app can display them.

## Supported formats

Currently the following formats are supported:

* CSV (requires columns in this order: track, time, lat, lon, elevation)
* [GPX](https://en.wikipedia.org/wiki/GPS_Exchange_Format)
* [TCX](https://en.wikipedia.org/wiki/Training_Center_XML)
* [KML](https://en.wikipedia.org/wiki/Keyhole_Markup_Language) (only `LineString` tag)

## Example usage

Using it with the provided `convert`/`convert.bat` scripts:

```bash
./convert --in_file test.tcx --in_format TCX --out_file out.gpx --out_format GPX
```

Calling the `Convert` class directly:

```bash
java -cp "./lib/*" com.github.fracpete.gpsformats4j.Convert \
  --in_file test.tcx --in_format TCX --out_file out.gpx --out_format GPX
```

Using Java code:

```java
import com.github.fracpete.gpsformats4j.Convert;
import com.github.fracpete.gpsformats4j.formats.GPX;
import com.github.fracpete.gpsformats4j.formats.TCX;
import java.io.File;

public class MyConversion {
  
  public static void main(String[] args) {
    Convert convert = new Convert();
    convert.setInputFile(new File("test.tcx"));
    convert.setInputFormat(TCX.class);
    convert.setOutputFile(new File("out.gpx"));
    convert.setOutputFormat(GPX.class);

    String msg = convert.execute();
    // successful if null returned:
    if (msg != null)
      System.err.println(msg);
  }
}
```

## Releases

The following releases are available:

* [0.0.7](https://github.com/fracpete/gpsformats4j/releases/download/gpsformats4j-0.0.7/gpsformats4j-0.0.7-bin.zip)
* [0.0.6](https://github.com/fracpete/gpsformats4j/releases/download/gpsformats4j-0.0.6/gpsformats4j-0.0.6-bin.zip)
* [0.0.5](https://github.com/fracpete/gpsformats4j/releases/download/gpsformats4j-0.0.5/gpsformats4j-0.0.5-bin.zip)
* [0.0.4](https://github.com/fracpete/gpsformats4j/releases/download/gpsformats4j-0.0.4/gpsformats4j-0.0.4-bin.zip)
* [0.0.2](https://github.com/fracpete/gpsformats4j/releases/download/v0.0.2/gpsformats4j-0.0.2-bin.zip)
* [0.0.1](https://github.com/fracpete/gpsformats4j/releases/download/v0.0.1/gpsformats4j-0.0.1-bin.zip)


## Maven

Use the following dependency to include the library in your Maven project:
```xml
    <dependency>
      <groupId>com.github.fracpete</groupId>
      <artifactId>gpsformats4j</artifactId>
      <version>0.0.7</version>
    </dependency>
```

# Tag - xml dsl for java

A tiny little dsl for java for creating simple xml.

Example:

    String xml =
        tag("foo").with(
            atr("bar", "baz"),
            tag("hello"),
            val("world"),
            tag("life").with(
                atr("isgood", "yes")))
        .toString();

Gives:

    <foo bar="baz"><hello/>world<life isgood="yes"/></foo>

Just import static the methods from com.alisk.xml.Tag.
There is also a toDoc() method.

Working with lists or other collections:

    List<String> list = Arrays.asList({"hello", "world"});
    String xml =
        tag("foo").with(
            atr("bar", "baz"),
            tagList(list, new Tag.Maker<String>() {
                public Tag make(String s) {
                    return tag("txt").with(val(s));
                }
            }))
        .toString();

Gives:

    <foo bar="baz"><txt>hello</txt><txt>world</txt></foo>

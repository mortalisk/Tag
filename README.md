# XmlDsl

A tiny little dsl for creating simple xml.

Example:

    String xml =
        tag("foo").with(
            atr("bar", "baz"),
                tag("hello"),
                val("world"),
                tag("life").with(
                    atr("isgood", "yes")))
        .toString();


Just import static the methods from com.alisk.xml.Tag.
There is also a toDoc() method.

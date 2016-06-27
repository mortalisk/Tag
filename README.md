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

Gives:

    <foo bar="baz"><hello/>world<life isgood="yes"/></foo>

Just import static the methods from com.alisk.xml.Tag.
There is also a toDoc() method.

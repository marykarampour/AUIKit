package com.prometheussoftware.auikit.model;

import com.prometheussoftware.auikit.common.Constants;

public class Text extends BaseModel {

    public enum REGEX_FORMAT {
        DEFAULT,
        INT,
        INT_POSITIVE,
        FLOAT,
        FLOAT_POSITIVE,
        ALPHABET,
        ALPHANUMERIC,
        EMAIL,
        PHONE,
        PASSWORD
    }

    public enum TEXT_FORMAT {

        //No change, the output string will be the same as input, example:
        //1. example_string -> example_string
        //2. exampleString -> exampleString
        None,

        //Capitaliazing the input string, example:
        //1. example_string -> Example_string
        //2. exampleString -> ExampleString
        Capitalized,

        //camelCasing the input string, example:
        //1. example_string -> exampleString
        //2. exampleString -> exampleString
        CamelCase,

        //camelCasing the input string, example:
        //1. example_string -> ExampleString
        //2. exampleString -> ExampleString
        //3. example_string_0 -> exampleString0
        CapitalizedCamelCase,

        //Upper case all charachters of input string, example:
        //1. example_string -> EXAMPLE_STRING
        //2. exampleString -> EXAMPLESTRING
        UpperCaseAll,

        //camelCase to under_score
        //1. exampleString -> example_string
        //2. exampleString0 -> example_string_0
        UnderScore,

        //camelCase to under_score with all characters uppercased
        //1. exampleString -> EXAMPLE_STRING
        //2. exampleString0 -> EXAMPLE_STRING_0
        UnderScoreUpperCaseAll,

        //camelCase to under_score - this option will not underscore digits
        //1. exampleString -> example_string
        //2. exampleString0 -> example_string0
        UnderScoreIgnoreDigits,

        //camelCase to under_score with all characters uppercased - this option will not underscore digits
        //1. exampleString -> EXAMPLE_STRING
        //2. exampleString0 -> EXAMPLE_STRING0
        UnderScoreIgnoreDigitsUpperCaseAll,
    }

    /** Pass NOT_FOUND_ID for no check for length */
    public int maxChars = Constants.NOT_FOUND_ID;
    /** Pass 0 for no check for length */
    public int minChars;
    public REGEX_FORMAT type;

    public static class Builder {

        private Text text;

        public Builder() {
            text = new Text();
        }

        public Builder setMaxChars(int maxChars) {
            text.maxChars = maxChars;
            return this;
        }

        public Builder setMinChars(int minChars) {
            text.minChars = minChars;
            return this;
        }

        public Builder setType(REGEX_FORMAT type) {
            text.type = type;
            return this;
        }

        public Text build() {
            return text;
        }
    }

    public static class Field extends BaseModel {

        public String placeholder;
        public Text textObject;
        public IndexPath path;

        public static class Builder {

            private Field field;

            public Builder() {
                field = new Field();
            }

            public Builder setPlaceholder(String placeholder) {
                field.placeholder = placeholder;
                return this;
            }

            public Builder setTextObject(Text textObject) {
                field.textObject = textObject;
                return this;
            }

            public Builder setPath(IndexPath path) {
                field.path = path;
                return this;
            }

            public Field build() {
                return field;
            }
        }
    }

    public static class Data extends BaseModel {

        public Field object;
        public String key;
        public String invalidMessage;
        /** Set to false to ignore setting this value in key, used for confirmation fields
         * default is true */
        public boolean setValue;
        public String value;

        public static class Builder {

            private Data data;

            public Builder() {
                data = new Data();
            }

            public Builder setObject(Field object) {
                data.object = object;
                data.setValue = true;
                return this;
            }

            public Builder setKey(String key) {
                data.key = key;
                return this;
            }

            public Builder setInvalidMessage(String invalidMessage) {
                data.invalidMessage = invalidMessage;
                return this;
            }

            public Builder setSetValue(boolean setValue) {
                data.setValue = setValue;
                return this;
            }

            public Builder setValue(String value) {
                data.value = value;
                return this;
            }

            public Data build() {
                return data;
            }
        }
    }
}

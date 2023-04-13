package dev.dotspace.squidly.response.analysis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion.VersionFlag;
import dev.dotspace.squidly.response.AnalysisResult;

import java.util.HashSet;
import java.util.Set;

public class JsonResponseAnalyzerImpl<T> implements JsonResponseAnalyzer {

    private final Class<T> tClass;
    private final JsonSchema schema;
    private final ObjectMapper objectMapper;
    private final boolean allowEmptyResponse, expectArray, expectObject, expectSingleObjectArray;

    private JsonResponseAnalyzerImpl(String schema, VersionFlag specVersion, Class<T> tClass) {
        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(specVersion);
        this.schema = factory.getSchema(getClass().getClassLoader().getResourceAsStream(schema));
        this.objectMapper = new ObjectMapper();
        this.tClass = tClass;
        this.expectObject = true;
        this.expectArray = false;
        this.expectSingleObjectArray = false;
        this.allowEmptyResponse = false;
    }

    private JsonResponseAnalyzerImpl(JsonSchema schema, boolean allowEmptyResponse, ObjectMapper mapper, boolean expectObject, boolean expectArray, boolean expectSingleObjectArray, Class<T> tClass) {
        this.schema = schema;
        this.allowEmptyResponse = allowEmptyResponse;
        this.objectMapper = mapper;
        this.expectObject = expectObject;
        this.expectArray = expectArray;
        this.expectSingleObjectArray = expectSingleObjectArray;
        this.tClass = tClass;
    }

    @Override
    public AnalysisResult<T> analyse(JsonNode jsonNode) {
        var errors = schema.validate(jsonNode);
        var ret_msg = "N/A";

        if (!errors.isEmpty()) {
            errors.forEach(System.err::println);
            System.err.printf("Could not validate against: %s%n%s%n", this.schema, jsonNode);
            return AnalysisResult.SCHEMA_MISMATCH;
        }

        if ((expectArray || expectSingleObjectArray) && jsonNode.isObject())
            return AnalysisResult.ERROR;

        if (expectObject && jsonNode.isArray())
            return AnalysisResult.ERROR;

        if (expectArray && jsonNode.size() < 1)
            return AnalysisResult.ERROR;

        if (expectSingleObjectArray && jsonNode.size() != 1)
            return AnalysisResult.ERROR;


        if (expectSingleObjectArray && jsonNode.isArray() && jsonNode.size() == 1)
            jsonNode = jsonNode.get(0);

        if (jsonNode.has("ret_msg"))
            ret_msg = jsonNode.get("ret_msg").asText("Null - Ok");
        else if (expectArray && jsonNode.get(0).has("ret_msg"))
            ret_msg = jsonNode.get(0).get("ret_msg").asText("Null - Ok");

        try {
            return new AnalysisResult<>(
                    objectMapper.treeToValue(jsonNode, tClass),
                    ret_msg,
                    true
            );
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
            return AnalysisResult.ERROR;
        }
    }

    public static class Builder<T> {
        private Class<T> returnTypeClass;
        private JsonSchema schema;
        private String path;
        private VersionFlag versionFlag = VersionFlag.V7;
        private boolean allowEmptyResponse;
        private final Set<Module> jacksonModules;

        private boolean expectArray;
        private boolean expectObject;
        private boolean expectSingleObjectArray;

        public Builder() {
            this.jacksonModules = new HashSet<>();
        }

        public Builder<T> type(Class<T> returnTypeClass) {
            this.returnTypeClass = returnTypeClass;
            return this;
        }

        public Builder<T> version(VersionFlag versionFlag) {
            this.versionFlag = versionFlag;
            return this;
        }

        public Builder<T> schema(String path) {
            this.path = path;
            return this;
        }

        public Builder<T> schema(JsonSchema jsonSchema) {
            this.schema = jsonSchema;
            return this;
        }

        public Builder<T> allowEmptyResponse() {
            this.allowEmptyResponse = true;
            return this;
        }

        public Builder<T> jacksonModule(Module module) {
            this.jacksonModules.add(module);
            return this;
        }

        public Builder<T> expectArray() {
            this.expectArray = true;
            this.expectObject = false;
            this.expectSingleObjectArray = false;
            return this;
        }

        public Builder<T> expectObject() {
            this.expectObject = true;
            this.expectArray = false;
            this.expectSingleObjectArray = false;
            return this;
        }

        public Builder<T> expectSingleObjectArray() {
            this.expectSingleObjectArray = true;
            this.expectObject = true;
            this.expectArray = false;
            return this;
        }

        public JsonResponseAnalyzerImpl<T> build() {
            if (this.schema == null) {
                var factory = JsonSchemaFactory.getInstance(this.versionFlag);
                this.schema = factory.getSchema(getClass().getClassLoader().getResourceAsStream(path));
            }

            var mapper = new ObjectMapper().registerModules(this.jacksonModules);

            return new JsonResponseAnalyzerImpl<T>(this.schema, this.allowEmptyResponse, mapper, this.expectObject, this.expectArray, this.expectSingleObjectArray, returnTypeClass);
        }
    }

}

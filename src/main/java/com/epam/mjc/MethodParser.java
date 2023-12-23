package com.epam.mjc;

import java.util.*;

public class MethodParser {

    /**
     * Parses string that represents a method signature and stores all it's members into a {@link MethodSignature} object.
     * signatureString is a java-like method signature with following parts:
     *      1. access modifier - optional, followed by space: ' '
     *      2. return type - followed by space: ' '
     *      3. method name
     *      4. arguments - surrounded with braces: '()' and separated by commas: ','
     * Each argument consists of argument type and argument name, separated by space: ' '.
     * Examples:
     *      accessModifier returnType methodName(argumentType1 argumentName1, argumentType2 argumentName2)
     *      private void log(String value)
     *      Vector3 distort(int x, int y, int z, float magnitude)
     *      public DateTime getCurrentDateTime()
     *
     * @param signatureString source string to parse
     * @return {@link MethodSignature} object filled with parsed values from source string
     */
    public MethodSignature parseFunction(String signatureString) {
        List<String> funcParts = stringToList(signatureString, "()");
        String signaturePart = funcParts.get(0);
        String argumentPart = funcParts.size() > 1? funcParts.get(1) : "";

        return craftMethodSignature(signaturePart, argumentPart);
    }

    private MethodSignature craftMethodSignature(String description, String arguments) {
        MethodSignature result;
        List<MethodSignature.Argument> args;
        String methodName, methodReturnType, methodAccessModifier;

        methodReturnType= "";
        methodAccessModifier= "";
        methodName = "";
        args = new ArrayList<>();

        List<String> descriptionStrings = stringToList(description, " ");

        if (descriptionStrings.size() >= 2) {
            methodReturnType = descriptionStrings.get(descriptionStrings.size() - 2);
            methodName = descriptionStrings.get(descriptionStrings.size() - 1);
            if (descriptionStrings.size() == 3) {
                methodAccessModifier = descriptionStrings.get(0);
            }
        }

        if(!arguments.isEmpty()){
            List<String> argTypeNamePairs = stringToList(arguments, " ,");
            args = craftArgs(argTypeNamePairs);
        }

        result = arguments.isEmpty()? new MethodSignature(methodName):new MethodSignature(methodName, args);
        result.setReturnType(methodReturnType);

        if(!methodAccessModifier.isEmpty()){
            result.setAccessModifier(methodAccessModifier);
        }

        return result;
    }

    private List<String> stringToList(String description, String delimiter) {
        List<String> result = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(description, delimiter, false);

        while(tokenizer.hasMoreTokens()){
            result.add(tokenizer.nextToken());
        }

        return  result;
    }

    private List<MethodSignature.Argument> craftArgs(List<String> argTypeNamePairs) {
        List<MethodSignature.Argument> result =new ArrayList<>();

        for(int i = 0; i < argTypeNamePairs.size()-1; i+=2){
            result.add(new MethodSignature.Argument(argTypeNamePairs.get(i), argTypeNamePairs.get(i + 1)));
        }

        return  result;
    }
}

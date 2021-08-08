package me.tofpu.speedbridge.filetype.type.type;

public class ReturnType {
    public static ReturnType of(final Object input){
        return new ReturnType().put(input);
    }
    private Object input;

    public ReturnType put(final Object input){
        this.input = input;
        return this;
    }

    public Object accept(){
        return input;
    }
}

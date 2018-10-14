package com.sentimentanalysis.usq.sentimentanalysis;

public class GraphException extends  Exception
{
    private String errorMessage;
    private Throwable cause;

    public static String badLengths = "Lengths do not match!";

    public GraphException()
    {
        super();
    }

    public GraphException(String message , Throwable cause)
    {
        super(message, cause);

        this.errorMessage = message;
        this.cause = cause;
    }


}

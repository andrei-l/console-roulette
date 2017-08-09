package com.github.al.roulette.roulette.mockito;


import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class IterativeAnswer implements Answer {
    private Answer[] answers;
    private int answerNum;

    public IterativeAnswer(Answer... answers) {
        this.answers = answers;
    }

    @Override
    public Object answer(InvocationOnMock invocation) throws Throwable {
        return answers[answerNum++].answer(invocation);
    }
}
package com.dellas.app.util;

import java.util.ArrayList;
import java.util.List;

import com.dellas.app.exception.MultipleTaskException;
import com.dellas.app.exception.TaskException;

/**
 * Classe para varrer a excecao e retornar os erros
 */
public class TaskExceptionHandler {

	public static List<String> getExcetionError(final Exception e){
		final List<String> excessoes= new ArrayList<>();
		if (e instanceof MultipleTaskException) {
			final MultipleTaskException me = (MultipleTaskException) e;
			for (final TaskException exception : me.getExceptions()) {
				excessoes.add(exception.getMessage());
			}
		}else if (e instanceof TaskException){
			final TaskException exception = (TaskException) e;
			excessoes.add(exception.getMessage());
		}else{
			excessoes.add("erro generico");
		}
		return excessoes;
	}
}
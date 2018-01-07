package nl.androidappfactory.recipe.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by jt on 6/21/17.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class NotesCommand {
	private String id;
	private String recipeNotes;

}

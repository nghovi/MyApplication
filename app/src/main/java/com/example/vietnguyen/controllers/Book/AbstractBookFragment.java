package com.example.vietnguyen.controllers.Book;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.vietnguyen.Const;
import com.example.vietnguyen.core.controllers.DialogBuilder;
import com.example.vietnguyen.core.controllers.MyFragment;
import com.example.vietnguyen.core.network.Api;
import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.models.Book;
import com.example.vietnguyen.models.MyModel;
import com.example.vietnguyen.myapplication.R;

import org.json.JSONObject;

public class AbstractBookFragment extends MyFragment{

	public final static String	KEY_UPDATED_BOOK	= "book_updated";
	protected Book				book;
	protected String			originBookStr;
	protected String			newWord;

	@Override
	protected void buildLayout(){
		super.buildLayout();
		builFoldActionAndOnClickEvents();
	}

	protected void builFoldActionAndOnClickEvents(){
		setFoldAction(getView(R.id.lnr_sbe_icon_url_selectable), getImageView(R.id.img_sbe_icon_url_fold_icon), R.id.edt_sbe_icon_url, null);
		setFoldAction(getView(R.id.lnr_sbe_comment_selectable), getImageView(R.id.img_sbe_fold), R.id.edt_sbe_comment, null);
		setFoldAction(getView(R.id.lnr_sbe_name_selectable), getImageView(R.id.img_sbe_name_fold_icon), R.id.edt_sbe_name, null);
		setFoldAction(getView(R.id.lnr_sbe_author_selectable), getImageView(R.id.img_sbe_author_fold_icon), R.id.edt_sbe_author, null);
		setFoldAction(getView(R.id.lnr_sbe_mood_selectable), getImageView(R.id.img_sbe_mood_fold_icon), R.id.edt_sbe_mood, null);
		setFoldAction(getView(R.id.lnr_sbe_link_selectable), getImageView(R.id.img_sbe_link_fold_icon), R.id.edt_sbe_link, null);
		setFoldAction(getView(R.id.lnr_sbe_name_selectable), getImageView(R.id.img_sbe_name_fold_icon), R.id.edt_sbe_name, null);

		setOnClickFor(R.id.ico_sbe_add_vocabulary, new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				showDialogForAddingWord();
			}
		});
	}

	private void addTextWatcherForBookImage(){
		getEditText(R.id.edt_sbe_icon_url).addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2){

			}

			@Override
			public void afterTextChanged(Editable editable){
				String url = editable.toString();
				MU.picassaLoadImage(editable.toString(), getImageView(R.id.img_sbe_image), activity);
			}
		});
	}

	private void buildVocabulary(){
		LayoutInflater inflater = (LayoutInflater)activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
		LinearLayout lnrVocabulary = getLinearLayout(R.id.lnr_sbe_vocabulary_list);
		lnrVocabulary.removeAllViews();
		RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		for(final String word : this.book.getWords()){
			View itemBookWordEdit = inflater.inflate(R.layout.item_book_word_edit, null);

			setTextFor(itemBookWordEdit, R.id.txt_ibwe_word, word);
			setOnClickFor(itemBookWordEdit, R.id.lnr_ibwe_add_phrase, new View.OnClickListener() {

				@Override
				public void onClick(View view){
					showDialogForAddingPhrase(word);
				}
			});
			setOnClickFor(itemBookWordEdit, R.id.img_ibwe_delete, new View.OnClickListener() {

				@Override
				public void onClick(View view) {
					showDialogConfirmDeleteWord(word);
				}
			});
			builPhrasesForWord(word, itemBookWordEdit, inflater);
			lnrVocabulary.addView(itemBookWordEdit);
		}
	}

	private void builPhrasesForWord(final String word, View itemBookWord, LayoutInflater inflater){
		List<String> phrases = this.book.getWordUsage(word);
		setFoldAction(getView(itemBookWord, R.id.lnr_ibwe), getImageView(itemBookWord, R.id.img_ibwe_fold), R.id.lnr_ibwe_foldable, getView(itemBookWord, R.id.img_ibwe_delete));
		setFoldAction(getView(itemBookWord, R.id.lnr_ibwe), getImageView(itemBookWord, R.id.img_ibwe_fold), R.id.lnr_ibwe_foldable, getView(itemBookWord, R.id.img_ibwe_delete));
		LinearLayout lnrPhrases = getLinearLayout(itemBookWord, R.id.lnr_ibwe_phrases);
		lnrPhrases.removeAllViews();
		for(final String phrase : phrases){
			View line = inflater.inflate(R.layout.item_phrase_edit, null);
			setTextFor(line, R.id.txt_ipe_phrase, phrase);
			setOnClickFor(line, R.id.img_ipe_delete, new View.OnClickListener() {

				@Override
				public void onClick(View view){
					dlgBuilder.buildConfirmDlgTopDown("Cancel", "Delete", new View.OnClickListener() {

						@Override
						public void onClick(View view){
							deletePhrase(word, phrase);
						}
					}).show();
				}
			});
			lnrPhrases.addView(line);
		}
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////////

	protected void showDialogForAddingWord(){
		dlgBuilder.buildDialogWith2Edt(activity, "Enter new word", "Enter new phrase", new DialogBuilder.OnDialogWithEdtDismiss() {

			@Override
			public void onClickDone(String input1, String input2){
				addWordForBook(input1, input2);
			}
		}).show();
	}

	protected void addWordForBook(String newWord, String newPhrase){
		if(!MU.isEmpty(newWord)){
			if(book.hasWord(newWord)){
				addPhraseForWord(newWord, newPhrase);
			}else{
				List<Book> booksContainWord = BookSearchFragment.searchWord(newWord);
				if(booksContainWord.size() > 0){
					addPhraseForExistingWord(booksContainWord, newWord, newPhrase);
				}else{
					book.addWordForBook(newWord);
					saveThisBookAndStay();
					addPhraseForWord(newWord, newPhrase);
				}
			}
		}
	}

	private void addPhraseForExistingWord(List<Book> booksContainWord, String newWord, String newPhrase){
		Book b = booksContainWord.get(0);
		if(!MU.isEmpty(newPhrase)){
			b.addPhraseForWord(newWord, newPhrase);
			book.save();
			saveBookToServer(b, false);
			showExistedWordNotifyDialog("Added new phrase for '" + newWord + "' at '" + b.name + "'", newWord, b);
		}else{
			showExistedWordNotifyDialog("Found '" + newWord + "' at '" + b.name + "'", newWord, b);
		}
	}

	private void showExistedWordNotifyDialog(String msg, String newWord, final Book foundBook){
		dlgBuilder.build2OptionsDialog(activity, "Word existed!", msg, "View", new View.OnClickListener() {

			@Override
			public void onClick(View view){
				activity.addFragment(new BookDetailFragment(), AbstractBookFragment.KEY_UPDATED_BOOK, foundBook);
			}
		}).show();
	}

	protected void showDialogConfirmDeleteWord(final String word){
		dlgBuilder.buildConfirmDlgTopDown("Cancel", "Delete", new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				book.deleteWord(word);
				saveThisBookAndStay();
			}
		}).show();
	}

	protected void showDialogForAddingPhrase(final String word){
		Log.e("lllllll", "wow");
		dlgBuilder.buildDialogWithEdt(activity, "Enter new phrase for " + word, null, new DialogBuilder.OnDialogWithEdtDismiss() {

			@Override
			public void onClickDone(String input1, String input2) {
				addPhraseForWord(word, input1);
			}
		}).show();
	}

	protected void addPhraseForWord(String word, String phrase){
		if(book.hasWord(word) && !MU.isEmpty(phrase)){
			book.addPhraseForWord(word, phrase);
			saveThisBookAndStay();
		}
	}

	protected void deletePhrase(String word, String phrase){
		book.deletePhraseForWord(word, phrase);
		saveThisBookAndStay();
	}

	public void saveThisBookAndStay(){
		saveThisBookToServer(false);
	}

	public void saveThisBookToServerAndBack(){
		saveThisBookToServer(true);
	}

	public void saveThisBookToServer(boolean willBack){
		buildBookFromLayout();
		originBookStr = book.toString();
		book.save();

		rebuildLayoutOrBack(willBack);
//		saveBookToServer(book, willBack);
	}

	public void saveBookToServer(final Book b, final boolean willBack){
		JSONObject params = MU.buildJsonObj(Arrays.<String>asList("book", b.toString()));
		postApi(Const.EDIT_BOOK, params, new Api.OnCallApiListener() {

			@Override
			public void onApiResponse(JSONObject response) {
				b.isRemoteSaved = true;
				b.save();
				showShortToast("Successfully saved changes");
//				rebuildLayoutOrBack(willBack);
			}

			@Override
			public void onApiError(String errorMsg) {
				b.isRemoteSaved = false;
				b.save();
//				rebuildLayoutOrBack(willBack);
			}
		});
	}

	private void rebuildLayoutOrBack(boolean willBack){
		if(willBack){
			activity.backToFragment(BookDetailFragment.class, BookDetailFragment.KEY_UPDATED_BOOK, book);
		}else{
			Log.e("888888", "so still book = " + book.toString());
			buildLayout();
		}
	}

//	public void deleteThisBook(){
//		dlgBuilder.buildConfirmDlgTopDown("Cancel", "Delete", new View.OnClickListener() {
//
//			@Override
//			public void onClick(View view){
//				deleteThisBookToServer();
//			}
//		}).show();
//	}

//	public void deleteThisBookToServer(){
//		JSONObject params = MU.buildJsonObj(Arrays.<String>asList("id", book.id));
//		postApi(Const.DELETE_BOOK, params, new Api.OnCallApiListener() {
//
//			@Override
//			public void onApiResponse(JSONObject response){
//				book.delete();
//				showShortToast("Successfully delete '" + book.name + "'");
//				activity.backToFragment(BookListFragment.class);
//			}
//
//			@Override
//			public void onApiError(String errorMsg){
//
//			}
//		});
//	}

	protected void buildBookFromLayout(){
		book.name = getEditText(R.id.edt_sbe_name).getText().toString();
		book.comment = getEditText(R.id.edt_sbe_comment).getText().toString();
		book.author = getEditText(R.id.edt_sbe_author).getText().toString();
		book.mood = getEditText(R.id.edt_sbe_mood).getText().toString();
		book.iconUrl = getEditText(R.id.edt_sbe_icon_url).getText().toString();
		book.link = getEditText(R.id.edt_sbe_link).getText().toString();
	}

	public void setBook(Book book){
		this.book = book;
		this.originBookStr = this.book.toString();
	}

	public static List<MyModel> searchWithConditions(Map<String, Object> conditions){
		String word = (String)conditions.get(BookSearchFragment.KEY_BOOK_SEARCH_WORD);
		String name = (String)conditions.get(BookSearchFragment.KEY_BOOK_SEARCH_NAME);
		String author = (String)conditions.get(BookSearchFragment.KEY_BOOK_SEARCH_AUTHOR);
		String comment = (String)conditions.get(BookSearchFragment.KEY_BOOK_SEARCH_COMMENT);

		List<MyModel> books = Book.getAllUndeleted(Book.class);
		Iterator<MyModel> ib = books.iterator();
		while(ib.hasNext()){
			Book book = (Book) ib.next();
			if(!MU.isEmpty(word) && !book.hasWord(word)){
				ib.remove();
				continue;
			}
			if(!MU.isEmpty(name) && !MU.checkMatch(book.name, name)){
				ib.remove();
				continue;
			}
			if(!MU.isEmpty(author) && !MU.checkMatch(book.author, author)){
				ib.remove();
				continue;
			}
			if(!MU.isEmpty(comment) && !MU.checkMatch(book.comment, comment)){
				ib.remove();
				continue;
			}
		}
		return books;
	}
}

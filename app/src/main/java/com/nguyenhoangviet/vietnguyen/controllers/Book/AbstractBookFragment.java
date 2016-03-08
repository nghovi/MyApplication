package com.nguyenhoangviet.vietnguyen.controllers.Book;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.nguyenhoangviet.vietnguyen.Const;
import com.nguyenhoangviet.vietnguyen.controllers.FragmentOfMainActivity;
import com.nguyenhoangviet.vietnguyen.core.controller.DialogBuilder;
import com.nguyenhoangviet.vietnguyen.core.network.Api;
import com.nguyenhoangviet.vietnguyen.core.utils.MU;
import com.nguyenhoangviet.vietnguyen.models.Book;
import com.nguyenhoangviet.vietnguyen.models.Link;
import com.nguyenhoangviet.vietnguyen.models.Phrase;
import com.nguyenhoangviet.vietnguyen.models.Word;
import com.nguyenhoangviet.vietnguyen.myapplication.R;

import org.json.JSONObject;

public abstract class AbstractBookFragment extends FragmentOfMainActivity implements View.OnClickListener{

	public final static String	KEY_UPDATED_BOOK				= "book_updated";
	public final static String	BOOK_COVER_PREFIX				= "book_cover";
	public final static int		REQ_CODE_ACTIVITY_SELECT_IMAGE	= 101;
	protected Book				book;
	protected String			newWord;

	@Override
	protected void buildLayout(){
		super.buildLayout();
		builFoldActionAndOnClickEvents();
		buildCoverImage();
		buildBookInfo();
		buildVocabulary();
	}

	abstract protected void buildBookInfo();

	abstract protected void buildVocabulary();

	protected void builFoldActionAndOnClickEvents(){
		setFoldAction(getView(R.id.lnr_sbe_icon_url_selectable), getImageView(R.id.img_sbe_icon_url_fold_icon), R.id.edt_sbe_icon_url, null, null);
		setFoldAction(getView(R.id.lnr_sbe_comment_selectable), getImageView(R.id.img_sbe_fold), R.id.edt_sbe_comment, null, null);
		setFoldAction(getView(R.id.lnr_sbe_name_selectable), getImageView(R.id.img_sbe_name_fold_icon), R.id.edt_sbe_name, null, null);
		setFoldAction(getView(R.id.lnr_sbe_author_selectable), getImageView(R.id.img_sbe_author_fold_icon), R.id.edt_sbe_author, null, null);
		setFoldAction(getView(R.id.lnr_sbe_mood_selectable), getImageView(R.id.img_sbe_mood_fold_icon), R.id.edt_sbe_mood, null, null);
		setFoldAction(getView(R.id.lnr_sbe_link_selectable), getImageView(R.id.img_sbe_link_fold_icon), R.id.edt_sbe_link, null, null);
		setFoldAction(getView(R.id.lnr_sbe_name_selectable), getImageView(R.id.img_sbe_name_fold_icon), R.id.edt_sbe_name, null, null);
		setOnClickFor(R.id.ico_sbe_add_vocabulary, this);
	}

	protected void buildCoverImage(){
		addTextWatcherForBookImage();
		MU.loadImage(activity, book.iconurl, getBookImageFileName(book), getImageView(R.id.img_sbe_image));
		setOnClickFor(R.id.img_sbe_image, new View.OnClickListener() {

			@Override
			public void onClick(View v){
				selectBookCover();
			}
		});
	}

	private void selectBookCover(){
		Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(i, REQ_CODE_ACTIVITY_SELECT_IMAGE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent){
		super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

		switch(requestCode){
		case REQ_CODE_ACTIVITY_SELECT_IMAGE:
			if(resultCode == Activity.RESULT_OK){
				Uri selectedImage = imageReturnedIntent.getData();

				Bitmap selectedBookCover = null;
				try{
					selectedBookCover = decodeUri(selectedImage);
					MU.saveBitMapImage(selectedBookCover, getBookImageFileName(book), activity);
					getImageView(R.id.img_sbe_image).setImageBitmap(selectedBookCover);
				}catch(FileNotFoundException e){
					e.printStackTrace();
				}
			}
		}
	}

	private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException{

		// Decode image size
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(selectedImage), null, o);

		// The new size we want to scale to
		final int REQUIRED_SIZE = 140;

		// Find the correct scale value. It should be the power of 2.
		int width_tmp = o.outWidth, height_tmp = o.outHeight;
		int scale = 1;
		while(true){
			if(width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE){
				break;
			}
			width_tmp /= 2;
			height_tmp /= 2;
			scale *= 2;
		}

		// Decode with inSampleSize
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		return BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(selectedImage), null, o2);

	}

	@Override
	public void onClick(View view){
		switch(view.getId()){
		case R.id.txt_fragment_book_edit_done:
			savedBookFromLayout(false);
			break;
		case R.id.txt_fba_done:
			savedBookFromLayout(true);
			break;
		case R.id.ico_sbe_add_vocabulary:
			showDialogForAddingWord();
			break;
		default:
			break;
		}
	}

	protected void addTextWatcherForBookImage(){
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
				if(!MU.isEmpty(url) && !url.equals(book.iconurl)){
					book.iconurl = url;
					MU.picassaLoadAndSaveImage(book.iconurl, getImageView(R.id.img_sbe_image), activity, AbstractBookFragment.getBookImageFileName(book));
				}else{
					MU.loadImage(activity, book.iconurl, getBookImageFileName(book), getImageView(R.id.img_sbe_image));
				}
			}
		});
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////////

	protected void showDialogForAddingWord(){
		dlgBuilder.buildAndShowDialogWith2Edt(getString(R.string.fragment_abstract_book_dlg_enter_new_word_msg1), getString(R.string.fragment_abstract_book_dlg_enter_new_word_msg2), new DialogBuilder.OnDialogWithEdtDismiss() {

			@Override
			public void onClickDone(String input1, String input2){
				addWordForBook(input1, input2);
			}
		});
	}

	private void showExistedWordNotifyDialog(String msg, String newWord, final Book foundBook){
		dlgBuilder.build2OptionsDialog(activity, getString(R.string.fragment_abstract_book_dlg_existing_word_msg), msg, getString(R.string.fragment_abstract_book_dlg_existing_word_btn1), new View.OnClickListener() {

			@Override
			public void onClick(View view){
				activity.addFragment(new BookDetailFragment(), AbstractBookFragment.KEY_UPDATED_BOOK, foundBook);
			}
		}).show();
	}

	protected void showDialogConfirmDeleteWord(final Word word){
		dlgBuilder.buildConfirmDlgTopDown(getString(R.string.cancel), getString(R.string.delete), new View.OnClickListener() {

			@Override
			public void onClick(View view){
				sendDeleteWord(word);
			}
		}).show();
	}

	protected void sendDeleteWord(final Word word){
		callPostApi(Const.DELETE_WORD, getJsonBuilder().add("word_id", word.id).getJsonObj(), new Api.OnApiSuccessObserver() {

			@Override
			public void onSuccess(JSONObject response){
				onSendDeleteWordSuccess(word, response);
			}

			@Override
			public void onFailure(JSONObject response){
				commonApiFailure(response);
			}
		});
	}

	private void onSendDeleteWordSuccess(Word deletedWord, JSONObject response){
		book.words.remove(deletedWord);
		buildVocabulary();
	}

	protected void showDialogForAddingPhrase(final Word word){
		dlgBuilder.buildAndShowDialogWithEdt(getString(R.string.fragment_abstract_book_dlg_enter_new_phrase_msg, word.syllabus), null, new DialogBuilder.OnDialogWithEdtDismiss() {

			@Override
			public void onClickDone(String input1, String input2){
				addPhraseForWord(word, input1);
				buildVocabulary();
			}
		});
	}

	protected void addPhraseForWord(final Word word, String phrase){
		if(!MU.isEmpty(phrase)){
			callPostApi(Const.ADD_PHRASE, getJsonBuilder().add("word_id", word.id).add("new_phrase", phrase).getJsonObj(), new Api.OnApiSuccessObserver() {

				@Override
				public void onSuccess(JSONObject response){
					onAddPhraseSuccess(word, response);
				}

				@Override
				public void onFailure(JSONObject response){
					commonApiFailure(response);
				}
			});
		}
	}

	private void onAddPhraseSuccess(Word modifedWord, JSONObject response){
		Phrase newPhrase = MU.convertToModel(response.optString("data"), Phrase.class);
		modifedWord.phrases.add(newPhrase);
		buildVocabulary();
	}

	protected void addWordForBook(final String syllabus, String content){
		if(!MU.isEmpty(syllabus)){
			Word wordExisted = book.findWord(syllabus);
			if(wordExisted != null){
				addPhraseForWord(wordExisted, content);
			}else{
				callPostApi(Const.ADD_WORD, getJsonBuilder().add("book_id", book.id).add("new_word", syllabus).add("new_phrase", content).getJsonObj(), new Api.OnApiSuccessObserver() {

					@Override
					public void onSuccess(JSONObject response){
						onAddWordForBookSuccess(response);
					}

					@Override
					public void onFailure(JSONObject response){
						commonApiFailure(response);
					}
				});
			}
		}
	}

	private void onAddWordForBookSuccess(JSONObject response){
		Word word = MU.convertToModel(response.optString("data"), Word.class);
		book.words.add(0, word);
		buildVocabulary();
	}

	protected void savedBookFromLayout(boolean isNew){
		book.name = getEditText(R.id.edt_sbe_name).getText().toString();
		book.comment = getEditText(R.id.edt_sbe_comment).getText().toString();
		book.author = getEditText(R.id.edt_sbe_author).getText().toString();
		book.iconurl = getEditText(R.id.edt_sbe_icon_url).getText().toString();
		if(book.link != null){
			book.link.url = getEditText(R.id.edt_sbe_link).getText().toString();
		}else{
			book.link = new Link();
			book.link.url = getEditText(R.id.edt_sbe_link).getText().toString();
		}

		if(isNew){
			sendSavingBook(Const.ADD_BOOK, book.name, book.comment, book.author, book.iconurl, book.link);
		}else{
			sendSavingBook(Const.EDIT_BOOK, book.name, book.comment, book.author, book.iconurl, book.link);
		}
	}

	protected void sendSavingBook(String url, String name, String comment, String author, String iconurl, Link link){
		callPostApi(url, getJsonBuilder().add("id", book.id).add("name", name).add("comment", comment).add("author", author).add("iconurl", iconurl).add("link", link.url).getJsonObj(), new Api.OnApiSuccessObserver() {

			@Override
			public void onSuccess(JSONObject response){
				onSendSavingBookSuccess(response);
			}

			@Override
			public void onFailure(JSONObject response){
				commonApiFailure(response);
			}
		});
	}

	private void onSendSavingBookSuccess(JSONObject response){
		book = MU.convertToModel(response.optString("data"), Book.class);
		this.activity.backOneFragment();
	}

	protected boolean hasChangeData(){
		return (book.author != null && !getEditText(R.id.edt_sbe_author).getText().toString().equals(book.author)) || (book.name != null && !getEditText(R.id.edt_sbe_name).getText().toString().equals(book.name)) || (book.link != null && !getEditText(R.id.edt_sbe_link).getText().toString().equals(book.link.url)) || (book.comment != null && !getEditText(R.id.edt_sbe_comment).getText().toString().equals(book.comment)) || !getEditText(R.id.edt_sbe_icon_url).getText().toString().equals(book.iconurl);
	}

	public static String getBookImageFileName(Book book){
		return BOOK_COVER_PREFIX + book.id;
	}

	public void setBook(Book book){
		this.book = book;
	}

	public static List<Book> searchWithConditions(Map<String, Object> conditions, List<Book> books){
		String word = (String)conditions.get(BookSearchFragment.KEY_BOOK_SEARCH_WORD);
		String phrase = (String)conditions.get(BookSearchFragment.KEY_BOOK_SEARCH_PHRASE);
		String name = (String)conditions.get(BookSearchFragment.KEY_BOOK_SEARCH_NAME);
		String author = (String)conditions.get(BookSearchFragment.KEY_BOOK_SEARCH_AUTHOR);
		String comment = (String)conditions.get(BookSearchFragment.KEY_BOOK_SEARCH_COMMENT);

		Iterator<Book> ib = books.iterator();
		while(ib.hasNext()){
			Book book = (Book)ib.next();
			if(!MU.isEmpty(word) && book.findWord(word) == null){
				ib.remove();
				continue;
			}
			if(!MU.isEmpty(phrase) && !book.hasPhrase(phrase)){
				ib.remove();
				continue;
			}
			// if(!MU.isEmpty(name) && !MU.checkMatch(book.name, name)){
			// ib.remove();
			// continue;
			// }
			// if(!MU.isEmpty(author) && !MU.checkMatch(book.author, author)){
			// ib.remove();
			// continue;
			// }
			// if(!MU.isEmpty(comment) && !MU.checkMatch(book.comment, comment)){
			// ib.remove();
			// continue;
			// }
		}
		return books;
	}
}

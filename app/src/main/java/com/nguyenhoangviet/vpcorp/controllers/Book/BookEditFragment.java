package com.nguyenhoangviet.vpcorp.controllers.Book;

import java.util.List;

import com.nguyenhoangviet.vpcorp.core.utils.MU;
import com.nguyenhoangviet.vpcorp.models.Book;
import com.nguyenhoangviet.vpcorp.vnote2.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class BookEditFragment extends AbstractBookFragment implements View.OnClickListener{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_book_edit, container, false);
	}

	@Override
	protected void buildLayout(){
		super.buildLayout();
		getMainActivity().footer.hide();
	}

	@Override
	protected void buildBookInfo(){
		setTextFor(R.id.edt_sbe_name, book.name);
		setTextFor(R.id.edt_sbe_link, book.link);
		setTextFor(R.id.edt_sbe_author, book.author);
		setTextFor(R.id.edt_sbe_comment, book.comment);
		setTextFor(R.id.edt_sbe_icon_url, book.iconUrl);
		setTextFor(R.id.edt_sbe_mood, book.mood);
	}

	@Override
	protected void buildVocabulary(){
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
				public void onClick(View view){
					showDialogConfirmDeleteWord(word);
				}
			});
			builPhrasesForWord(word, itemBookWordEdit, inflater);
			lnrVocabulary.addView(itemBookWordEdit);
		}
	}

	@Override
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
					savedBookFromLayout();
					addPhraseForWord(newWord, newPhrase);
				}
			}
			buildVocabulary();
		}
	}

	private void builPhrasesForWord(final String word, View itemBookWord, LayoutInflater inflater){
		List<String> phrases = this.book.getWordUsage(word);
		setFoldAction2(getView(itemBookWord, R.id.lnr_ibwe), R.id.lnr_item_book_word_word, getImageView(itemBookWord, R.id.img_ibwe_fold), R.id.lnr_ibwe_foldable, getView(itemBookWord, R.id.img_ibwe_delete));
		LinearLayout lnrPhrases = getLinearLayout(itemBookWord, R.id.lnr_ibwe_phrases);
		lnrPhrases.removeAllViews();
		for(final String phrase : phrases){
			View line = inflater.inflate(R.layout.item_phrase_edit, null);
			setTextFor(line, R.id.txt_ipe_phrase, phrase);
			setOnClickFor(line, R.id.lnr_item_phrase_edit, new View.OnClickListener() {

				@Override
				public void onClick(View view){
					showConfirmdDlgDeletePhrase(word, phrase);
				}
			});
			lnrPhrases.addView(line);
		}
	}

	private void showConfirmdDlgDeletePhrase(final String word, final String phrase){
		dlgBuilder.buildConfirmDlgTopDown(getString(R.string.cancel), getString(R.string.delete), new View.OnClickListener() {

			@Override
			public void onClick(View view){
				deletePhrase(word, phrase);
			}
		}).show();
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	protected void onClickBackBtn(){
		hideSofeKeyboard();
		if(!hasChangeData()){
			activity.backToFragment(BookDetailFragment.class, AbstractBookFragment.KEY_UPDATED_BOOK, book);
		}else{
			dlgBuilder.build2OptsDlgTopDown(getString(R.string.discard), getString(R.string.save), new View.OnClickListener() {

				@Override
				public void onClick(View view){
					activity.backOneFragment();
				}
			}, new View.OnClickListener() {

				@Override
				public void onClick(View view){
					savedBookFromLayout();
					activity.backToFragment(BookDetailFragment.class, AbstractBookFragment.KEY_UPDATED_BOOK, book);
				}
			}).show();
		}
	}

}

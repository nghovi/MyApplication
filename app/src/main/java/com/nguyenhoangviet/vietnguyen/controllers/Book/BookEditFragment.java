package com.nguyenhoangviet.vietnguyen.controllers.Book;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nguyenhoangviet.vietnguyen.Const;
import com.nguyenhoangviet.vietnguyen.core.network.Api;
import com.nguyenhoangviet.vietnguyen.models.Phrase;
import com.nguyenhoangviet.vietnguyen.models.Word;
import com.nguyenhoangviet.vietnguyen.myapplication.R;

import org.json.JSONObject;

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
		// LinearLayout lnrContent = (LinearLayout)getView().findViewById(R.id.lnr_sbe_main_content);
		// JSONObject jsonObject = MU.buildJsonObjFromModel(book);
		// MU.interpolate(lnrContent, jsonObject);
		setOnClickFor(R.id.txt_fragment_book_edit_done, this);
		setTextFor(R.id.edt_sbe_name, book.name);
		setTextFor(R.id.edt_sbe_link, book.link.url);
		setTextFor(R.id.edt_sbe_author, book.author);
		setTextFor(R.id.edt_sbe_comment, book.comment);
		setTextFor(R.id.edt_sbe_icon_url, book.iconurl);
	}

	@Override
	protected void buildVocabulary(){
		LayoutInflater inflater = (LayoutInflater)activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
		LinearLayout lnrVocabulary = getLinearLayout(R.id.lnr_sbe_vocabulary_list);
		lnrVocabulary.removeAllViews();
		RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		for(final Word word : this.book.words){
			View itemBookWordEdit = inflater.inflate(R.layout.item_book_word_edit, null);

			setTextFor(itemBookWordEdit, R.id.txt_ibwe_word, word.syllabus);
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

	private void builPhrasesForWord(final Word word, View itemBookWord, LayoutInflater inflater){
		setFoldAction2(getView(itemBookWord, R.id.lnr_ibwe), R.id.lnr_item_book_word_word, getImageView(itemBookWord, R.id.img_ibwe_fold), R.id.lnr_ibwe_foldable, getView(itemBookWord, R.id.img_ibwe_delete));
		LinearLayout lnrPhrases = getLinearLayout(itemBookWord, R.id.lnr_ibwe_phrases);
		lnrPhrases.removeAllViews();
		if(word.phrases != null){
			for(final Phrase phrase : word.phrases){
				View line = inflater.inflate(R.layout.item_phrase_edit, null);
				setTextFor(line, R.id.txt_ipe_phrase, phrase.content);
				setOnClickFor(line, R.id.lnr_item_phrase_edit, new View.OnClickListener() {

					@Override
					public void onClick(View view){
						showConfirmdDlgDeletePhrase(word, phrase);
					}
				});
				lnrPhrases.addView(line);
			}
		}
	}

	private void showConfirmdDlgDeletePhrase(final Word word, final Phrase phrase){
		dlgBuilder.buildConfirmDlgTopDown(getString(R.string.cancel), getString(R.string.delete), new View.OnClickListener() {

			@Override
			public void onClick(View view){
				sendDeletePhrase(word, phrase);
			}
		}).show();
	}

	protected void sendDeletePhrase(final Word word, final Phrase phrase){
		callPostApi(Const.DELETE_PHRASE, getJsonBuilder().add("phrase_id", phrase.id).getJsonObj(), new Api.OnApiSuccessObserver() {

			@Override
			public void onSuccess(JSONObject response){
				onSendDeletePhraseSuccess(word, phrase, response);
			}

			@Override
			public void onFailure(JSONObject repsone){
				commonApiFailure(repsone);
			}
		});
	}

	private void onSendDeletePhraseSuccess(Word word, Phrase deletedPhrase, JSONObject response){
		word.phrases.remove(deletedPhrase);
		buildVocabulary();
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
					savedBookFromLayout(true);
				}
			}).show();
		}
	}

}

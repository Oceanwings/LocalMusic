package com.erich.android.music;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import com.hnapay.android.util.StringUtil;

public final class Music extends BaseMedia
{
	private String mPath = StringUtil.EMPTY_STRING;
	private String mAlbum = StringUtil.EMPTY_STRING;
	private long mAlbumId = INVALID_ID;
	private String mArtist = StringUtil.EMPTY_STRING;
	private long mArtistId = INVALID_ID;
	private String mComposer = StringUtil.EMPTY_STRING;
	private int mDuration;
	private int mTrack;
	private int mBookmark;

	Music( long id, String name )
	{
		super( id, name );
	}

	public String getPath( )
	{
		return this.mPath;
	}

	public String getAlbum( )
	{
		return this.mAlbum;
	}

	public long getAblumId( )
	{
		return this.mAlbumId;
	}

	public String getArtist( )
	{
		return this.mArtist;
	}

	public long getArtistId( )
	{
		return this.mArtistId;
	}

	public String getComposer( )
	{
		return this.mComposer;
	}

	public int getDuration( )
	{
		return this.mDuration;
	}

	public int getTrack( )
	{
		return this.mTrack;
	}

	public int getBookmark( )
	{
		return this.mBookmark;
	}

	public Uri getContentUri( )
	{
		return MediaStore.Audio.Media.getContentUriForPath( this.mPath );
	}

	@Override
	public String toString( )
	{
		return "Music [ID=" + this.getId( ) + ", Name=" + this.getName( ) + ", Path=" + this.mPath
				+ ", Album=" + this.mAlbum + ", AblumId=" + this.mAlbumId + ", Artist="
				+ this.mArtist + ", ArtistId=" + this.mArtistId + ", Composer=" + this.mComposer
				+ ", Duration=" + this.mDuration + ", Track=" + this.mTrack + ", Bookmark="
				+ this.mBookmark + ']';
	}

	@Override
	public int hashCode( )
	{
		int result = super.hashCode( );
		result = ( ( result << 5 ) - result ) + this.mPath.hashCode( );
		result = ( ( result << 5 ) - result ) + Long.valueOf( this.mAlbumId ).hashCode( );
		result = ( ( result << 5 ) - result ) + this.mAlbum.hashCode( );
		result = ( ( result << 5 ) - result ) + Long.valueOf( this.mArtistId ).hashCode( );
		result = ( ( result << 5 ) - result ) + this.mArtist.hashCode( );
		result = ( ( result << 5 ) - result ) + this.mComposer.hashCode( );
		result = ( ( result << 5 ) - result ) + this.mDuration;
		result = ( ( result << 5 ) - result ) + this.mTrack;
		result = ( ( result << 5 ) - result ) + this.mBookmark;
		return result;
	}

	@Override
	public boolean equals( Object obj )
	{
		if ( !super.equals( obj ) || this.getClass( ) != obj.getClass( ) )
		{
			return false;
		}

		Music other = ( Music ) obj;
		return StringUtil.equals( this.mPath, other.mPath )
				&& StringUtil.equals( this.mAlbum, other.mAlbum )
				&& ( this.mAlbumId == other.mAlbumId )
				&& StringUtil.equals( this.mArtist, other.mArtist )
				&& ( this.mArtistId == other.mArtistId )
				&& StringUtil.equals( this.mComposer, other.mComposer )
				&& ( this.mDuration == other.mDuration ) && ( this.mTrack == other.mTrack )
				&& ( this.mBookmark == other.mBookmark );
	}

	static Music fromContentCursor( Cursor cursor )
	{
		if ( cursor == null || cursor.getColumnCount( ) == 0 || cursor.getCount( ) == 0 )
		{
			return null;
		}

		final long id = cursor.getLong( cursor.getColumnIndex( MediaStore.Audio.Media._ID ) );
		final String name = cursor
				.getString( cursor.getColumnIndex( MediaStore.Audio.Media.TITLE ) );
		Music music = new Music( id, name );

		int index = cursor.getColumnIndex( MediaStore.Audio.Media.DATA );
		if ( index > -1 )
		{
			music.mPath = cursor.getString( index );
		}

		index = cursor.getColumnIndex( MediaStore.Audio.Media.ALBUM_ID );
		if ( index > -1 )
		{
			music.mAlbumId = cursor.getInt( index );
		}

		index = cursor.getColumnIndex( MediaStore.Audio.Media.ALBUM );
		if ( index > -1 )
		{
			music.mAlbum = cursor.getString( index );
		}

		index = cursor.getColumnIndex( MediaStore.Audio.Media.ARTIST_ID );
		if ( index > -1 )
		{
			music.mArtistId = cursor.getInt( index );
		}

		index = cursor.getColumnIndex( MediaStore.Audio.Media.ARTIST );
		if ( index > -1 )
		{
			music.mArtist = cursor.getString( index );
		}

		index = cursor.getColumnIndex( MediaStore.Audio.Media.COMPOSER );
		if ( index > -1 )
		{
			music.mComposer = cursor.getString( index );
		}

		index = cursor.getColumnIndex( MediaStore.Audio.Media.DURATION );
		if ( index > -1 )
		{
			music.mDuration = cursor.getInt( index );
		}

		index = cursor.getColumnIndex( MediaStore.Audio.Media.TRACK );
		if ( index > -1 )
		{
			music.mTrack = cursor.getInt( index );
		}

		index = cursor.getColumnIndex( MediaStore.Audio.Media.BOOKMARK );
		if ( index > -1 )
		{
			music.mBookmark = cursor.getInt( index );
		}

		return music;
	}
}
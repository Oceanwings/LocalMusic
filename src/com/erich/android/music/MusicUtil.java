package com.erich.android.music;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;

public final class MusicUtil
{
	private static final String[ ] PROJECTION_MUSIC_DETAIL = new String[ ] {
			MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.TITLE,
			MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.ALBUM,
			MediaStore.Audio.Media.ALBUM_ID, MediaStore.Audio.Media.ARTIST,
			MediaStore.Audio.Media.ARTIST_ID, MediaStore.Audio.Media.COMPOSER,
			MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.TRACK,
			MediaStore.Audio.Media.BOOKMARK };

	private static final String[ ] PROJECTION_MUSIC_ID = new String[ ] { MediaStore.Audio.Media._ID };

	private static final String[ ] PROJECTION_PLAY_LIST = new String[ ] {
			MediaStore.Audio.Playlists._ID, MediaStore.Audio.Playlists.NAME };

	private static final String[ ] PROJECT_PLAY_LIST_DETAIL = new String[ ] {
			MediaStore.Audio.Playlists.Members.AUDIO_ID,
			MediaStore.Audio.Playlists.Members.PLAY_ORDER };

	private static final String[ ] PROJECTION_ALBUM = new String[ ] { MediaStore.Audio.Albums._ID,
			MediaStore.Audio.Albums.ALBUM, MediaStore.Audio.Albums.ALBUM_KEY,
			MediaStore.Audio.Albums.ALBUM_ART, MediaStore.Audio.Albums.NUMBER_OF_SONGS,
			MediaStore.Audio.AudioColumns.ARTIST_ID, MediaStore.Audio.Albums.ARTIST,
			MediaStore.Audio.AudioColumns.ARTIST_KEY };

	private static final String[ ] PROJECTION_ALBUM_ARTIST = new String[ ] {
			MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_KEY };

	private static final String[ ] PROJECTION_ARTIST = new String[ ] {
			MediaStore.Audio.Artists._ID, MediaStore.Audio.Artists.ARTIST,
			MediaStore.Audio.Artists.ARTIST_KEY, MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
			MediaStore.Audio.Artists.NUMBER_OF_TRACKS };

	private static final String WHERE_PARAMETER = "=?";
	private static final String WHERE_AND = " AND ";

	private static final String WHERE_IS_MUSIC = new StringBuilder( MediaStore.Audio.Media.IS_MUSIC )
			.append( "=1" ).toString( );

	private static final String VOLUME_NAME_EXTERNAL = "external";

	private static final String URI_ALBUM_ART = "content://media/external/audio/albumart";
	private static final String URI_MUSIC_ART = "content://media/external/audio/media/";
	private static final String URI_MUSIC_ART_SUFFIX = "/albumart";

	private static final int INVALID_INDEX = -1;
	private static final int INVALID_ID = -1;

	private MusicUtil( )
	{}

	public static List< Music > loadAllMusic( Context context )
	{
		return loadMusic( context, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI );
		// return loadMusic( context,
		// MediaStore.Audio.Media.INTERNAL_CONTENT_URI );
	}

	private static List< Music > loadMusic( Context context, Uri uri )
	{
		if ( context == null || uri == null )
		{
			return Collections.emptyList( );
		}

		final Cursor cursor = context
				.getApplicationContext( )
				.getContentResolver( )
				.query( uri, PROJECTION_MUSIC_DETAIL, WHERE_IS_MUSIC, null,
						MediaStore.Audio.Media.DEFAULT_SORT_ORDER );

		if ( cursor == null || cursor.getCount( ) == 0 || !cursor.moveToFirst( ) )
		{
			if ( cursor != null )
			{
				cursor.close( );
			}
			return Collections.emptyList( );
		}

		ArrayList< Music > musicList = new ArrayList< Music >( cursor.getCount( ) );
		Music music = null;
		do
		{
			music = Music.fromContentCursor( cursor );
			if ( music != null && music.hasValidId( ) )
			{
				musicList.add( music );
			}
		}
		while ( cursor.moveToNext( ) );

		cursor.close( );
		return Collections.unmodifiableList( musicList );
	}

	public static List< Album > loadAllAlbums( Context context )
	{
		return loadAllAlbums( context, MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI );
		// return loadAllAlbums( context,
		// MediaStore.Audio.Albums.INTERNAL_CONTENT_URI );
	}

	private static List< Album > loadAllAlbums( Context context, Uri uri )
	{
		if ( context == null || uri == null )
		{
			return Collections.emptyList( );
		}

		final Cursor cursor = context
				.getApplicationContext( )
				.getContentResolver( )
				.query( uri, PROJECTION_ALBUM, null, null,
						MediaStore.Audio.Albums.DEFAULT_SORT_ORDER );

		if ( cursor == null || cursor.getCount( ) == 0 || !cursor.moveToFirst( ) )
		{
			if ( cursor != null )
			{
				cursor.close( );
			}
			return Collections.emptyList( );
		}

		ArrayList< Album > albumList = new ArrayList< Album >( cursor.getCount( ) );
		Album album = null;

		do
		{
			album = new Album(
					cursor.getInt( cursor.getColumnIndex( MediaStore.Audio.Albums._ID ) ),
					cursor.getString( cursor.getColumnIndex( MediaStore.Audio.Albums.ALBUM ) ),
					cursor.getString( cursor.getColumnIndex( MediaStore.Audio.Albums.ALBUM_KEY ) ),
					cursor.getInt( cursor.getColumnIndex( MediaStore.Audio.AudioColumns.ARTIST_ID ) ) );

			if ( album.hasValidId( ) )
			{
				album.addAllSubItemIds( loadMusicIdsByAlbum( context, album.getId( ) ) );
				albumList.add( album );
			}
		}
		while ( cursor.moveToNext( ) );

		cursor.close( );
		return Collections.unmodifiableList( albumList );
	}

	private static long[ ] loadMusicIdsByAlbum( Context context, final long albumId )
	{
		if ( context == null || albumId <= INVALID_ID )
		{
			return new long[ 0 ];
		}

		final String selection = new StringBuilder( MediaStore.Audio.Media.ALBUM_ID )
				.append( WHERE_PARAMETER ).append( WHERE_AND ).append( WHERE_IS_MUSIC ).toString( );
		final String sortOrder = new StringBuilder( MediaStore.Audio.Media.ALBUM_KEY ).append( ',' )
				.append( MediaStore.Audio.Media.TRACK ).toString( );

		final Cursor cursor = context
				.getApplicationContext( )
				.getContentResolver( )
				.query( MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, PROJECTION_MUSIC_ID,
						selection, new String[ ] { String.valueOf( albumId ) }, sortOrder );

		if ( cursor == null || cursor.getCount( ) == 0 || !cursor.moveToFirst( ) )
		{
			if ( cursor != null )
			{
				cursor.close( );
			}
			return new long[ 0 ];
		}

		final long[ ] ids = new long[ cursor.getCount( ) ];
		int columnIndex = INVALID_INDEX;
		int index = 0;
		do
		{
			columnIndex = cursor.getColumnIndex( MediaStore.Audio.Media._ID );
			ids[ index++ ] = ( index > INVALID_INDEX ) ? cursor.getLong( columnIndex ) : INVALID_ID;
		}
		while ( cursor.moveToNext( ) );

		cursor.close( );
		return ids;
	}

	public static List< Artist > loadAllArtists( Context context )
	{
		return loadAllArtists( context, MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI );
		// return loadAllArtists( context,
		// MediaStore.Audio.Albums.INTERNAL_CONTENT_URI );
	}

	private static List< Artist > loadAllArtists( Context context, Uri uri )
	{
		if ( context == null || uri == null )
		{
			return Collections.emptyList( );
		}

		final Cursor cursor = context
				.getApplicationContext( )
				.getContentResolver( )
				.query( uri, PROJECTION_ARTIST, null, null,
						MediaStore.Audio.Artists.DEFAULT_SORT_ORDER );

		if ( cursor == null || cursor.getCount( ) == 0 || !cursor.moveToFirst( ) )
		{
			if ( cursor != null )
			{
				cursor.close( );
			}
			return Collections.emptyList( );
		}

		ArrayList< Artist > artistList = new ArrayList< Artist >( cursor.getCount( ) );
		Artist artist = null;
		do
		{
			artist = new Artist( cursor.getInt( cursor
					.getColumnIndex( MediaStore.Audio.Artists._ID ) ), cursor.getString( cursor
					.getColumnIndex( MediaStore.Audio.Artists.ARTIST ) ), cursor.getString( cursor
					.getColumnIndex( MediaStore.Audio.Artists.ARTIST_KEY ) ) );

			if ( artist.hasValidId( ) )
			{
				artist.addAllSubItemIds( loadAlbumsIdsByArtist( context, artist.getId( ) ) );
				artistList.add( artist );
			}
		}
		while ( cursor.moveToNext( ) );

		cursor.close( );
		return artistList;
	}

	private static long[ ] loadAlbumsIdsByArtist( Context context, final long artistId )
	{
		if ( context == null || artistId <= INVALID_ID )
		{
			return new long[ 0 ];
		}

		final Cursor cursor = context
				.getApplicationContext( )
				.getContentResolver( )
				.query( MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, PROJECTION_ALBUM_ARTIST,
						MediaStore.Audio.AudioColumns.ARTIST_ID + WHERE_PARAMETER,
						new String[ ] { String.valueOf( artistId ) },
						MediaStore.Audio.Albums.DEFAULT_SORT_ORDER );

		if ( cursor == null || cursor.getCount( ) == 0 || !cursor.moveToFirst( ) )
		{
			if ( cursor != null )
			{
				cursor.close( );
			}
			return new long[ 0 ];
		}

		final long[ ] ids = new long[ cursor.getCount( ) ];
		int columnIndex = INVALID_INDEX;
		int index = 0;
		do
		{
			columnIndex = cursor.getColumnIndex( MediaStore.Audio.Albums._ID );
			ids[ index++ ] = ( index > INVALID_INDEX ) ? cursor.getLong( columnIndex ) : INVALID_ID;
		}
		while ( cursor.moveToNext( ) );

		cursor.close( );
		return ids;
	}

	public static List< Playlist > loadAllPlaylists( Context context )
	{
		return loadAllPlaylists( context, MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI );
		// return loadAllPlaylists( context,
		// MediaStore.Audio.Playlists.INTERNAL_CONTENT_URI );
	}

	private static List< Playlist > loadAllPlaylists( Context context, Uri uri )
	{
		if ( context == null || uri == null )
		{
			return Collections.emptyList( );
		}

		final Cursor cursor = context
				.getApplicationContext( )
				.getContentResolver( )
				.query( uri, PROJECTION_PLAY_LIST, null, null,
						MediaStore.Audio.Playlists.DEFAULT_SORT_ORDER );

		if ( cursor == null || cursor.getCount( ) == 0 || !cursor.moveToFirst( ) )
		{
			if ( cursor != null )
			{
				cursor.close( );
			}
			return Collections.emptyList( );
		}

		ArrayList< Playlist > playlistList = new ArrayList< Playlist >( cursor.getCount( ) );
		Playlist playlist = null;
		do
		{
			playlist = new Playlist( cursor.getInt( cursor
					.getColumnIndex( MediaStore.Audio.Playlists._ID ) ), cursor.getString( cursor
					.getColumnIndex( MediaStore.Audio.Playlists.NAME ) ) );

			if ( playlist.hasValidId( ) )
			{
				playlist.addAllSubItemIds( loadMusicIdsByPlaylistId( context, playlist.getId( ) ) );
				playlistList.add( playlist );
			}
		}
		while ( cursor.moveToNext( ) );

		cursor.close( );
		return playlistList;
	}

	private static long[ ] loadMusicIdsByPlaylistId( Context context, final long playlistId )
	{
		if ( context == null || playlistId <= INVALID_ID )
		{
			return new long[ 0 ];
		}

		final Cursor cursor = context
				.getApplicationContext( )
				.getContentResolver( )
				.query( MediaStore.Audio.Playlists.Members.getContentUri( VOLUME_NAME_EXTERNAL,
						playlistId ), PROJECT_PLAY_LIST_DETAIL, null, null,
						MediaStore.Audio.Playlists.Members.DEFAULT_SORT_ORDER );

		if ( cursor == null || cursor.getCount( ) == 0 || !cursor.moveToFirst( ) )
		{
			if ( cursor != null )
			{
				cursor.close( );
			}
			return new long[ 0 ];
		}

		final long[ ] ids = new long[ cursor.getCount( ) ];
		int columnIndex = INVALID_INDEX;
		int index = 0;
		do
		{
			columnIndex = cursor.getColumnIndex( MediaStore.Audio.Playlists.Members.AUDIO_ID );
			ids[ index++ ] = ( index > INVALID_INDEX ) ? cursor.getLong( columnIndex ) : INVALID_ID;
		}
		while ( cursor.moveToNext( ) );

		cursor.close( );
		return ids;
	}

	public static Bitmap getArtwork( Context context, final long musicId, final long albumId )
	{
		if ( context == null )
		{
			throw new IllegalArgumentException(
					"Could NOT get audio artwork due to context is NULL!" );
		}

		context = context.getApplicationContext( );

		if ( albumId < 0 )
		{
			return getArtworkFromFile( context, musicId );
		}

		Bitmap bitmap = null;
		try
		{
			bitmap = MediaStore.Images.Media.getBitmap( context.getContentResolver( ),
					ContentUris.withAppendedId( Uri.parse( URI_ALBUM_ART ), albumId ) );
		}
		catch ( FileNotFoundException e )
		{
			e.printStackTrace( );
			bitmap = null;
		}
		catch ( IOException e )
		{
			e.printStackTrace( );
			bitmap = null;
		}

		if ( bitmap == null )
		{
			bitmap = getArtworkFromFile( context, musicId );
		}

		return bitmap;
	}

	private static Bitmap getArtworkFromFile( Context context, final long musicId )
	{
		if ( musicId < 0 )
		{
			return null;
		}

		Bitmap bitmap = null;
		try
		{
			ParcelFileDescriptor pfd = context.getContentResolver( ).openFileDescriptor(
					Uri.parse( URI_MUSIC_ART + musicId + URI_MUSIC_ART_SUFFIX ), "r" );
			if ( pfd != null )
			{
				bitmap = BitmapFactory.decodeFileDescriptor( pfd.getFileDescriptor( ) );
			}
		}
		catch ( FileNotFoundException e )
		{
			e.printStackTrace( );
			bitmap = null;
		}

		return bitmap;
	}
}
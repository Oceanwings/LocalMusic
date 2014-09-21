package com.erich.android.music;

import android.content.Context;

public final class MediaManager
{
	private static MediaManager instance;

	public static MediaManager getInstance( )
	{
		if ( instance == null )
		{
			synchronized ( MediaManager.class )
			{
				if ( instance == null )
				{
					instance = new MediaManager( );
				}
			}
		}
		return instance;
	}

	private MediaTable< Music > mMusicTable = new MediaTable< Music >( );
	private MediaTable< Playlist > mPlaylistTable = new MediaTable< Playlist >( );
	private MediaTable< Album > mAlbumTable = new MediaTable< Album >( );
	private MediaTable< Artist > mArtistTable = new MediaTable< Artist >( );

	private MediaManager( )
	{}

	public synchronized void init( Context context )
	{
		if ( context == null )
		{
			throw new IllegalArgumentException(
					"Could not initialize MediaManager using NULL context." );
		}

		this.clear( );

		this.mMusicTable.addAll( MusicUtil.loadAllMusic( context ) );
		this.mPlaylistTable.addAll( MusicUtil.loadAllPlaylists( context ) );
		this.mAlbumTable.addAll( MusicUtil.loadAllAlbums( context ) );
		this.mArtistTable.addAll( MusicUtil.loadAllArtists( context ) );
	}

	private synchronized void clear( )
	{
		this.mMusicTable.clear( );
		this.mPlaylistTable.clear( );
		this.mAlbumTable.clear( );
		this.mArtistTable.clear( );
	}

	public long[ ] getMusicIds( )
	{
		return this.mMusicTable.getIdArray( );
	}

	public Music getMusicById( final long id )
	{
		return this.mMusicTable.getById( id );
	}

	public long[ ] getPlayListIds( )
	{
		return this.mPlaylistTable.getIdArray( );
	}

	public Playlist getPlayListAt( final int location )
	{
		return this.mPlaylistTable.getDataAt( location );
	}

	public Playlist getPlayListById( final long id )
	{
		return this.mPlaylistTable.getById( id );
	}

	public long[ ] getAlbumIds( )
	{
		return this.mAlbumTable.getIdArray( );
	}

	public Album getAlbumAt( final int location )
	{
		return this.mAlbumTable.getDataAt( location );
	}

	public Album getAlbumById( final long id )
	{
		return this.mAlbumTable.getById( id );
	}

	public long[ ] getArtistIds( )
	{
		return this.mArtistTable.getIdArray( );
	}

	public Artist getArtistAt( final int location )
	{
		return this.mArtistTable.getDataAt( location );
	}

	public Artist getArtistById( final long id )
	{
		return this.mArtistTable.getById( id );
	}

	public Artist getArtistById( Long id )
	{
		return this.mArtistTable.getById( id );
	}
}
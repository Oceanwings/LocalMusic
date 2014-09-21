package com.erich.android.music;

import java.util.Arrays;

public final class Album extends MediaCollection
{
	// FIXME: There may be multiple artists in a single album.
	private final int mArtistId;

	public Album( int id, String name, String key, int artistId )
	{
		super( id, name, key );

		this.mArtistId = artistId;
	}

	public String getAlbumKey( )
	{
		return this.getKey( );
	}

	public int getArtistId( )
	{
		return this.mArtistId;
	}

	public int getMusicCount( )
	{
		return this.getSubItemCount( );
	}

	public long[ ] getAllMusicId( )
	{
		return this.getItemIds( );
	}

	public long getMusciIdAt( int location )
	{
		return this.getItemIdAt( location );
	}

	@Override
	public String toString( )
	{
		return "Album [ID=" + this.getId( ) + ", Name=" + this.getName( ) + ", Key="
				+ this.getKey( ) + ", Artist Id = " + this.mArtistId + ", Music Id List: "
				+ Arrays.toString( this.getAllMusicId( ) ) + ']';
	}

	@Override
	public int hashCode( )
	{
		int result = super.hashCode( );
		result = ( result << 5 ) - result + Long.valueOf( this.mArtistId ).hashCode( );
		return ( result << 5 ) - result + Album.class.hashCode( );
	}

	@Override
	public boolean equals( Object obj )
	{
		if ( !( super.equals( obj ) || this.getClass( ) != obj.getClass( ) ) )
		{
			return false;
		}

		Album other = ( Album ) obj;
		return ( this.mArtistId == other.mArtistId );
	}
}
package com.erich.android.music;

import java.util.Arrays;

public final class Artist extends MediaCollection
{
	public Artist( int id, String name, String key )
	{
		super( id, name, key );
	}

	public String getArtistKey( )
	{
		return this.getKey( );
	}

	public int getAlbumCount( )
	{
		return this.getSubItemCount( );
	}

	public long[ ] getAllAlbumId( )
	{
		return this.getItemIds( );
	}

	public long getAlbumIdAt( int location )
	{
		return this.getItemIdAt( location );
	}

	@Override
	public String toString( )
	{
		return "Artist [ID=" + this.getId( ) + ", Name=" + this.getName( ) + ", Key="
				+ this.getKey( ) + ", Album Id List: " + Arrays.toString( this.getAllAlbumId( ) )
				+ ']';
	}

	@Override
	public int hashCode( )
	{
		int result = super.hashCode( );
		return ( result << 5 ) - result + Artist.class.hashCode( );
	}

	@Override
	public boolean equals( Object obj )
	{
		return ( super.equals( obj ) && this.getClass( ) == obj.getClass( ) );
	}
}
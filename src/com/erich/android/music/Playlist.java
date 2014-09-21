package com.erich.android.music;

import java.util.Arrays;

public final class Playlist extends MediaCollection
{
	public Playlist( int id, String name )
	{
		super( id, name, "" );
	}

	public int getMusicCount( )
	{
		return this.getSubItemCount( );
	}

	public long[ ] getAllMusicId( )
	{
		return this.getItemIds( );
	}

	public long getMusicIdAt( int location )
	{
		return this.getItemIdAt( location );
	}

	@Override
	public String toString( )
	{
		return "Playlist [" + ", ID=" + this.getId( ) + ", Name=" + this.getName( )
				+ ", Music Id List: " + Arrays.toString( this.getAllMusicId( ) ) + ']';
	}

	@Override
	public int hashCode( )
	{
		int result = super.hashCode( );
		return ( result << 5 ) - result + Playlist.class.hashCode( );
	}

	@Override
	public boolean equals( Object obj )
	{
		return ( super.equals( obj ) && this.getClass( ) == obj.getClass( ) );
	}
}
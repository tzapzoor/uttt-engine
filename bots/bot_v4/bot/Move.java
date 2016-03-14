// // Copyright 2016 theaigames.com (developers@theaigames.com)

//    Licensed under the Apache License, Version 2.0 (the "License");
//    you may not use this file except in compliance with the License.
//    You may obtain a copy of the License at

//        http://www.apache.org/licenses/LICENSE-2.0

//    Unless required by applicable law or agreed to in writing, software
//    distributed under the License is distributed on an "AS IS" BASIS,
//    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//    See the License for the specific language governing permissions and
//    limitations under the License.
//  
//    For the full copyright and license information, please view the LICENSE
//    file that was distributed with this source code.

package bot;

/**
 * Move class
 * 
 * Stores a move.
 * 
 * @author Jim van Eeden <jim@starapple.nl>, Joost de Meij <joost@starapple.nl>
 */

public class Move {
	public int mCol, mRow;

	public Move() {
	}

	public Move(int i, int j) {
		mCol = j;
		mRow = i;
	}

	public int getCol() {
		return mCol;
	}

	public int getRow() {
		return mRow;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Move) {
			Move other = (Move) obj;
			if (this.mCol == other.mCol && this.mRow == other.mRow) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "("+mRow+", "+mCol+")";
	}
}

#!/usr/bin/env kotlin
/**
 * I couldn't be bothered to figure out how to write this in POSIX or Bash
 * So I just wrote it in an actually decent language
 */

import java.io.File
import java.nio.file.LinkOption
import kotlin.io.path.Path
import kotlin.io.path.forEachDirectoryEntry
import kotlin.io.path.isDirectory
import kotlin.io.path.name

val repoName = System.getenv("REPO_NAME") ?: "hyprland-void"
val packages: MutableMap<String, HashSet<String>> = HashMap()

// Get non-symlink directories in srcpkgs
Path("/work/$repoName/srcpkgs").forEachDirectoryEntry {
	if(it.isDirectory(LinkOption.NOFOLLOW_LINKS)) packages.put(it.name, HashSet())
}

// Populate package dependency lists
for(pkg in packages.keys) {
	val required = ArrayList<String>()
	var toNextLine = false

	// For each line check if it contains the keyword "depends" or is following from one which does
	Path("/work/$repoName/srcpkgs/$pkg/template").toFile().forEachLine {
		// If the previous line contained "depends" and an opening quote mark, but not a closing one
		if(toNextLine) {
			var line = it
			// If this line contains a closing quote mark
			if(it.contains('"')) {
				toNextLine = false
				line = it.substringBefore('"')
			}
			// Split line at any whitespace and add to the list
			line.split("\\s+".toRegex()).forEach(required::add)
		}

		// If line contains "depends" as a keyword and an opening quote mark
		if(it.contains("depends") && it.contains('"')) {
			var depends = it.substringAfter('"')
			if(!depends.contains('"')) toNextLine = true
			else depends = depends.substringBefore('"')
			depends.split("\\s+".toRegex()).forEach(required::add)
		}
	}

	// Add dependency to this package's list if the dependency is also present in srcpkgs
	required.forEach {
		if(packages.containsKey(it)) packages.get(pkg)!!.add(it)
	}
}

// Turn packages into an array so we can create a sorted array with indices
val temp = packages.keys.toTypedArray()
var order = arrayOfNulls<String>(temp.size)

fun Array<String?>.insert(position: Int, element: String): Array<String?> {
	return sliceArray(0 until position) + element + sliceArray(position until size)
}

for(i in temp.indices) {
	// Find reverse dependencies
	var last = false;
	for(j in order.indices) {
		// If array is empty, insert first element
		if(order[j].isNullOrBlank()) {
			order[j] = temp[i]
			break
		}

		// If the package has no dependencies, insert immediately
		if(packages.get(temp[i])!!.isEmpty()) {
			order = order.insert(0, temp[i])
			break
		}

		// If package at order[j] depends on package at temp[i], the package at temp[i] must be built first
		if(packages.get(order[j])!!.contains(temp[i])) {
			order = order.insert(j, temp[i])
			break
		}
	}
}


println("Build Order:")
for(i in order.indices) {
	if(order[i] == null) continue

	println("${i + 1}: ${order[i]} - ${packages.get(order[i])}")
	File("/work/build-order").appendText("${order[i]}\n")
}
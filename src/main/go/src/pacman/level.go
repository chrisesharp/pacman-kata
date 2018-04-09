package main

import (
	"strconv"
	"strings"
)

// LevelMap interface
type LevelMap interface {
	Unpack()
	Get(level int) string
	Max() int
}

type levelStruct struct {
	levelMaps string
	maxLevel  int
	levels    []string
}

// Unpack maps into individual level array
func (maps *levelStruct) Unpack() {
	maps.levels = strings.Split(maps.levelMaps, "SEPARATOR")
	maxLevel, err := strconv.Atoi(strings.Trim(maps.levels[0], "\n"))
	if err != nil {
		panic(err)
	}
	maps.maxLevel = maxLevel
}

// GetLevel
func (maps *levelStruct) Get(level int) string {
	return maps.levels[level]
}

// Max level
func (maps *levelStruct) Max() int {
	return maps.maxLevel
}

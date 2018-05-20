package scoreboard

import (
	"context"
	"fmt"
	sw "pacman/swagger"
)

// Client interface
type Client interface {
	PostScore(score int)
	Scores() []string
}

type scoreboardStruct struct {
	player string
	client *sw.APIClient
}

// PostScore to the scoreboard
func (sb *scoreboardStruct) PostScore(score int) {
	scoreToPost := (sw.Score{Player: sb.player, Score: int32(score)})
	sb.client.ScoreboardApi.AddScore(context.Background(), scoreToPost)
}

// Scores returns a list of top 10 scores
func (sb *scoreboardStruct) Scores() []string {
	var highscores []string
	scores, _, _ := sb.client.ScoreboardApi.Scores(context.Background())
	for _, score := range scores {
		scorestring := fmt.Sprintf("%s:%d", score.Player, score.Score)
		highscores = append(highscores, scorestring)
	}
	return highscores
}

// NewClient to a scoreboard service
func NewClient(scoreboardURL, player string) Client {
	cfg := sw.NewConfiguration()
	scoreboardClient := sw.NewAPIClient(cfg)
	scoreboardClient.ChangeBasePath(scoreboardURL)
	return &scoreboardStruct{client: scoreboardClient, player: player}
}

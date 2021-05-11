import React, {Component} from 'react';
import {
  ScrollView,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from 'react-native';

class HomeScreen extends Component {
  list = [
    {title: '语音识别', route: 'BaiduAsr'},
    {title: '语音唤醒', route: 'BaiduWakeUp'},
  ];

  handleItemPress = item => {
    this.props.navigation.navigate(item.route);
  };

  render() {
    return (
      <View style={styles.container}>
        <ScrollView contentContainerStyle={styles.container}>
          {this.list.map(l => (
            <TouchableOpacity
              onPress={() => this.handleItemPress(l)}
              key={l.route}>
              <View style={styles.itemView}>
                <Text style={styles.itemText}>{l.title}</Text>
              </View>
            </TouchableOpacity>
          ))}
        </ScrollView>
      </View>
    );
  }
}

export default HomeScreen;

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  itemView: {
    paddingVertical: 10,
    borderBottomWidth: 1,
    borderColor: '#dddddd',
    paddingLeft: 20,
  },
  itemText: {
    fontSize: 16,
  },
});

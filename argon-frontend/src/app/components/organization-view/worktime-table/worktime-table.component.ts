import { Component, Input, OnInit, Output, EventEmitter } from '@angular/core';
import { Worktime } from 'src/app/models/worktime.model';

@Component({
  selector: 'arg-worktime-table',
  templateUrl: './worktime-table.component.html',
  styleUrls: ['./worktime-table.component.css']
})
export class WorktimeTableComponent implements OnInit {
  @Input() worktimes: Worktime[];
  @Output() onWorktimeEdit = new EventEmitter<Worktime>();
  @Output() onWorktimeDelete = new EventEmitter<Worktime>();

  constructor() { }

  ngOnInit(): void {
  }

  deleteWorktime(worktime: Worktime) {
    this.onWorktimeDelete.emit(worktime);
  }

  editWorktime(worktime: Worktime) {
    this.onWorktimeEdit.emit(worktime);
  }

}
